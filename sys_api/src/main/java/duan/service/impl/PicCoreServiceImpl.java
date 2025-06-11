package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.entity.*;
import duan.mapper.PicAttributeMapper;
import duan.mapper.PicCoreMapper;
import duan.service.IPicCoreService;
import duan.utils.PicUtils;
import duan.utils.RedisUtil;
import duan.utils.UploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-10
 */
@Service
public class PicCoreServiceImpl extends ServiceImpl<PicCoreMapper, PicCore> implements IPicCoreService {
    private static final Logger logger = LoggerFactory.getLogger(PicCoreServiceImpl.class);

    @Value("${upload.temp_path}")
    private String temppicPath;
    @Value("${upload.local_perfix}")
    private String localPerfix;
    @Autowired
    private PicCoreMapper picCoreMapper;
    @Autowired
    private PicAttributeMapper picAttributeMapper;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private PicNumServiceImpl picNumService;
    @Autowired
    private PicTagServiceImpl picTagService;
    @Autowired
    private RedisUtil redisUtil;

    // --- 与 RandPicCacheTask 中缓存相关的常量保持一致 ---
    // Redis中存储随机图片列表的键
    private static final String RAND_PIC_KEY = "rand:pic:list";
    // 缓存的图片数量
    private static final int CACHE_SIZE = 200;
    // 缓存的有效期（秒）
    private static final int CACHE_EXPIRE = 300;
    // --- 动态刷新缓存相关的常量 ---
    // Redis中存储 /pic/rand 接口访问次数的键
    private static final String RAND_PIC_ACCESS_COUNT_KEY = "rand:pic:access_count";
    // 访问次数阈值，当达到此值时触发异步刷新
    private static final int ACCESS_COUNT_THRESHOLD = 10; // 例如：100次访问后触发
    // 访问次数计数器的时间窗口（秒）
    private static final int ACCESS_COUNT_WINDOW_SECONDS = 60; // 例如：60秒

    // --- Redis 分布式锁相关常量 ---
    // 定义分布式锁的Redis键名
    private static final String RAND_PIC_REFRESH_LOCK_KEY = "lock:rand_pic_refresh_cache";
    // 定义锁的值，可以是任意字符串，用于标识锁。更复杂的场景下可以使用UUID等唯一值来确保解锁的安全性。
    private static final String RAND_PIC_REFRESH_LOCK_VALUE = "refreshing";
    // 定义锁的过期时间（单位：秒）。
    // 这个时间需要仔细评估，应大于缓存刷新操作本身预计花费的最长时间，以防止正常操作未完成锁就过期。
    // 但也不能太长，以防获取锁的节点崩溃后，锁长时间不释放。
    private static final long RAND_PIC_REFRESH_LOCK_EXPIRE_SECONDS = 60; // 例如，设置为60秒


    /**
     *
     * @param file
     * @param picCore
     * @return 上传图片并生成缩略图，返回图片唯一的Pid
     * @throws IOException
     */
    @Override
    public Integer upload(MultipartFile file, PicCore picCore) throws IOException {
        //生成随机文件名
        String fileName = UploadUtils.getRandomImgName(file.getOriginalFilename());
        //本地上传到temp文件夹
        Path path = Paths.get(temppicPath,fileName);
        Files.write(path,file.getBytes());
        picCore.setFilename(fileName);
        picCore.setLocalurl(localPerfix+fileName);
        //缩略图文件格式统一为jpg
        String miniFileName = fileName.substring(0,fileName.lastIndexOf("."))+".jpg";
        picCore.setMiniurl(localPerfix+"thumbnail/thumbnail_"+miniFileName);

        picCoreMapper.insert(picCore);
        LambdaQueryWrapper<PicCore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PicCore::getFilename,fileName);
        picCore = picCoreMapper.selectOne(wrapper);
        SetPicAttribute(picCore,file);
        PicUtils.GenerateMiniImage(file,fileName);
        logger.info("上传图片成功，图片pid为："+picCore.getPid()+"，图片名为："+picCore.getFilename());


        return picCore.getPid();
    }

    @Override
    public List<PicDetail_VO> randPic(Integer num) {
        // --- 动态刷新缓存逻辑 ---
        // 记录接口访问次数
        long currentCount = redisUtil.incr(RAND_PIC_ACCESS_COUNT_KEY, 1);
        // 如果是时间窗口内的第一次访问（即键是新创建的），为计数器设置过期时间
        if (currentCount == 1) {
            redisUtil.expire(RAND_PIC_ACCESS_COUNT_KEY, ACCESS_COUNT_WINDOW_SECONDS);
        }
        // 检查是否达到刷新阈值
        if (currentCount >= ACCESS_COUNT_THRESHOLD) {
            logger.info("接口 /pic/rand 访问达到阈值 ({}/{})，尝试触发异步缓存刷新。", currentCount, ACCESS_COUNT_THRESHOLD);
            // 调用异步刷新方法
            refreshRandPicCacheAsync();
        }
        // --- 动态刷新缓存逻辑结束 ---

        if(redisUtil.lGetListSize(RAND_PIC_KEY) < num) {
            // 如果缓存中没有图片或数量不足，则从数据库中获取
            logger.warn("缓存未命中或数量不足 (需要 {}, 实际 {}), 从数据库获取", num, redisUtil.lGetListSize(RAND_PIC_KEY));
            return getrandPicFromDb(num); // 调用从数据库获取的方法
        }
        //todo 一段时间请求过多，需要重新生成缓存来避免图片一直重复 (当前通过动态刷新解决)
        long len = redisUtil.lGetListSize(RAND_PIC_KEY);
        // 如果实际缓存数量仍不足 (可能在计数和检查之间缓存被其他方式清空)
        if (len < num) {
            logger.warn("缓存数量再次检查不足 (需要 {}, 实际 {}), 从数据库获取", num, len);
            return getrandPicFromDb(num);
        }

        //每次请求时，先用 lLen 获取长度，然后用 lIndex(key, 随机下标) 随机取若干个元素
        Random random = new Random();
        Set<Integer> indexSet = new HashSet<>();
        // 确保随机索引不越界，并尝试获取 num 个不重复的索引
        // 如果 len 非常小，比如小于 num，这里可能会有性能问题或死循环，但前面的检查已覆盖 len < num 的情况
        int maxAttempts = num * 3; // 设置最大尝试次数，防止因意外情况导致死循环
        int attempts = 0;
        while (indexSet.size() < num && attempts < maxAttempts) {
            indexSet.add(random.nextInt((int) len));
            attempts++;
        }
        // 如果尝试多次后仍未获取足够索引 (理论上在 len >= num 时不应发生)
        if (indexSet.size() < num) {
            logger.warn("未能获取足够的随机索引 (需要 {}, 获取 {}), 可能缓存数量不足或并发问题, 从数据库获取", num, indexSet.size());
            return getrandPicFromDb(num); // 作为后备，从数据库获取
        }

        List<PicDetail_VO> result = new ArrayList<>();
        for (Integer idx : indexSet) {
            Object obj = redisUtil.lGetIndex(RAND_PIC_KEY, idx);
            if (obj instanceof PicDetail_VO) { // 增加类型检查
                 result.add((PicDetail_VO)obj);
            } else if (obj != null) {
                logger.warn("从Redis获取到非PicDetail_VO类型的对象: {}", obj.getClass().getName());
            }
        }
        // 如果从缓存中获取到的图片数量不足 (例如部分对象转换失败或为null)
        if (result.size() < num) {
            logger.warn("从缓存获取后，实际图片数量不足 (需要 {}, 获取 {}), 补充从数据库获取", num, result.size());
            List<PicDetail_VO> additionalPics = getrandPicFromDb(num - result.size());
            result.addAll(additionalPics);
        }
        return result;
    }

    /**
     * 从数据库获取指定数量的随机图片详情。
     * 此方法主要供缓存填充和缓存未命中时使用。
     * @param num 需要获取的图片数量
     * @return 图片详情列表
     */
    public List<PicDetail_VO> getrandPicFromDb(Integer num) {
        List<PicDetail_VO> list = new ArrayList<>();
        if (num <= 0) return list; // 如果请求数量不合法，返回空列表
        for (int i = 0; i < num; i++) {
            //随机获取一张图片
            PicDetail_VO picCore = picCoreMapper.selectRandPic();
            if (picCore != null) { // 确保查询结果不为null
                picCore.setTags(tagService.getTagsByPid(picCore.getPid()));
                list.add(picCore);
            } else {
                logger.warn("从数据库获取随机图片时返回了null (尝试获取第 {} 张)", i + 1);
            }
        }
        return list;
    }

    /**
     * 异步刷新随机图片缓存的方法。
     * 使用 @Async 注解使其在单独的线程中执行。
     * 通过 Redis 分布式锁防止并发执行此刷新操作。
     */
    @Async // 声明为异步方法
    public void refreshRandPicCacheAsync() {
        // 尝试获取Redis分布式锁
        // RAND_PIC_REFRESH_LOCK_KEY: 锁的唯一名称
        // RAND_PIC_REFRESH_LOCK_VALUE: 锁的值，当前实现中主要用于setIfAbsent的value参数
        // RAND_PIC_REFRESH_LOCK_EXPIRE_SECONDS: 锁的过期时间（秒）
        if (!redisUtil.tryLock(RAND_PIC_REFRESH_LOCK_KEY, RAND_PIC_REFRESH_LOCK_VALUE, RAND_PIC_REFRESH_LOCK_EXPIRE_SECONDS)) {
            // 如果 tryLock 返回 false，说明锁已被其他实例或线程持有
            logger.info("未能获取到Redis分布式锁 '{}'，另一个缓存刷新任务可能正在进行中。本次触发被跳过。", RAND_PIC_REFRESH_LOCK_KEY);
            return; // 获取锁失败，直接返回，避免重复刷新
        }

        // 成功获取到锁，执行后续操作
        try {
            logger.info("成功获取Redis分布式锁 '{}'，开始异步刷新随机图片缓存...", RAND_PIC_REFRESH_LOCK_KEY);

            // --- 开始执行核心的缓存刷新逻辑 ---
            List<PicDetail_VO> randPicList = this.getrandPicFromDb(CACHE_SIZE); // 从数据库获取新的随机图片列表

            if (randPicList != null && !randPicList.isEmpty()) {
                redisUtil.del(RAND_PIC_KEY); // 清空旧的图片列表缓存
                // 将新的图片列表推入Redis List
                // 注意：当前的 redisUtil.lSet 方法是逐个添加元素的。
                // 如果图片数量非常大，考虑优化为批量添加（例如使用 Redis 的 RPUSH 命令一次性添加多个元素）
                // 或者 pipeline 操作，以提高效率。
                for (PicDetail_VO pic : randPicList) {
                    redisUtil.lSet(RAND_PIC_KEY, pic); // 将每个图片对象存储到Redis列表中
                }
                // 为整个列表键设置过期时间
                redisUtil.expire(RAND_PIC_KEY, CACHE_EXPIRE);
                logger.info("成功将 {} 张图片刷新到Redis缓存 '{}'，并设置有效期为 {} 秒。", randPicList.size(), RAND_PIC_KEY, CACHE_EXPIRE);
            } else {
                logger.warn("从数据库获取的随机图片列表为空或null，未执行缓存刷新操作。");
            }

            // 缓存刷新（无论成功与否或是否有数据）后，重置接口访问计数器。
            // 这个操作放在锁内，确保与缓存刷新操作的“尝试”相关联。
            redisUtil.del(RAND_PIC_ACCESS_COUNT_KEY);
            logger.info("已重置接口访问计数器 '{}'。", RAND_PIC_ACCESS_COUNT_KEY);
            // --- 核心的缓存刷新逻辑结束 ---

            logger.info("异步刷新随机图片缓存任务完成。");

        } catch (Exception e) {
            // 捕获在缓存刷新过程中可能发生的任何异常
            logger.error("异步刷新随机图片缓存时发生严重错误，当前持有锁 '{}'。", RAND_PIC_REFRESH_LOCK_KEY, e);
            // 异常发生时，锁依然会在 finally 块中被释放
        } finally {
            // 无论缓存刷新是否成功，或者是否发生异常，都必须在 finally 块中释放锁
            // 这是保证分布式锁最终会被释放的关键步骤，防止死锁。
            redisUtil.unlock(RAND_PIC_REFRESH_LOCK_KEY);
            logger.info("已释放Redis分布式锁 '{}'。", RAND_PIC_REFRESH_LOCK_KEY);
        }
    }


    // getrandPic 方法名修改为 getrandPicFromDb 以明确其数据来源
    // 原 getrandPic(Integer num) 方法实现已合并到 getrandPicFromDb(Integer num)
    // 保留原始的 getrandPic 签名，但其实现现在是 getrandPicFromDb
    // 为了清晰，将原先的 public List<PicDetail_VO> getrandPic(Integer num) 重命名为 getrandPicFromDb
    // 而 randPic(Integer num) 是处理缓存逻辑的顶层方法。
    // 上述修改已在 randPic 和 getrandPicFromDb 方法中体现。

    @Override
    public PicDetail_VO getPic(Integer pid) {
//        Map<String,Object> map = MapUtils.toMapByJson(picCoreMapper.selectById(pid));
//        map.put("tags",tagService.getTagsByPid(pid));
        PicDetail_VO picCore = picCoreMapper.selectByPid(pid);
        if (picCore == null){
            throw new RuntimeException("图片不存在");
        }
        picCore.setTags(tagService.getTagsByPid(pid));
        picNumService.addSeeNum(pid);
        return picCore;
    }

    @Override
    public List<PicDetail_VO> getByTag(String tag, Integer num) {
        List<PicDetail_VO> list = new ArrayList<>();
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getTagName,tag);
        if(tagService.getOne(wrapper)==null){
            throw new RuntimeException("标签不存在");
        }
        Integer tagId = tagService.getOne(wrapper).getId();
        LambdaQueryWrapper<PicTag> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(PicTag::getTagId,tagId);
        long count = picTagService.count(wrapper1);
        if(count == 0){
            // 这里不应该抛出异常，而是返回空列表或提示没有该标签的图片
            logger.info("标签 '{}' 下没有图片。", tag);
            return list; // 返回空列表
        }
        // 如果请求的图片数量大于数据库中的图片数量，则返回数据库中的所有图片
        if(count<num){
            list=picCoreMapper.getAllPicByTag(tag);
            for(PicDetail_VO picCoreItem:list){ // 变量名修改避免与外部picCore冲突
                picCoreItem.setTags(tagService.getTagsByPid(picCoreItem.getPid()));
            }
            return list;
        }
        // 随机获取指定数量的图片
        List<PicDetail_VO> randomPicsByTag = picCoreMapper.selectRandPicListByTag(tag, num);
        if (randomPicsByTag != null && !randomPicsByTag.isEmpty()) {
            for (PicDetail_VO picCoreItem : randomPicsByTag) {
                picCoreItem.setTags(tagService.getTagsByPid(picCoreItem.getPid()));
                list.add(picCoreItem);
            }
        } else {
             logger.info("未能通过标签 '{}' 随机获取到图片。",tag);
        }
        return list;
    }

    @Override
    public void addPicTag(String tag, Integer pid) {
        //判断图片是否存在
        if(picCoreMapper.selectById(pid)==null){
            throw new RuntimeException("图片不存在");
        }
        //判断标签是否存在
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getTagName,tag);
        Tag existingTag = tagService.getOne(wrapper);
        Integer tagId;
        if(existingTag==null){
            //标签不存在则创建标签
            Tag newTag = new Tag();
            newTag.setTagName(tag);
            tagService.save(newTag);
            tagId = newTag.getId(); // 获取新创建标签的ID
        } else {
            tagId = existingTag.getId();
        }
        //判断图片是否已经有该标签
        LambdaQueryWrapper<PicTag> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(PicTag::getTagId,tagId);
        wrapper1.eq(PicTag::getPicId,pid);
        if(picTagService.getOne(wrapper1)!=null){
            throw new RuntimeException("图片已经有该标签");
        }
        //添加图片标签
        PicTag picTag = new PicTag();
        picTag.setPicId(pid);
        picTag.setTagId(tagId);
        picTagService.save(picTag);
    }

    @Override
    public void deletePicTag(String tag, Integer pid) {
        //判断图片是否存在
        if(picCoreMapper.selectById(pid)==null){
            throw new RuntimeException("图片不存在");
        }
        //判断标签是否存在
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getTagName,tag);
        Tag existingTag = tagService.getOne(wrapper);
        if(existingTag==null){
            throw new RuntimeException("标签不存在");
        }
        //获取标签id
        Integer tagId = existingTag.getId();
        //判断图片是否有该标签
        LambdaQueryWrapper<PicTag> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(PicTag::getTagId,tagId);
        wrapper1.eq(PicTag::getPicId,pid);
        if(picTagService.getOne(wrapper1)==null){
            throw new RuntimeException("图片没有该标签");
        }
        //删除图片标签
        picTagService.remove(wrapper1);
    }

    @Override
    public Long getPicCount() {
        return picCoreMapper.selectCount(null);
    }


    private void SetPicAttribute(PicCore picCore,MultipartFile file) throws IOException {
        try {
            PicAttribute picAttribute = new PicAttribute();
            picAttribute.setPid(picCore.getPid());
            picAttribute.setHeight(PicUtils.GetImageHeight(file));
            picAttribute.setWidth(PicUtils.GetImageWidth(file));
            picAttribute.setSize(PicUtils.GetImageSize(file));
            picAttribute.setUpdatetime(LocalDateTime.now());
            picAttributeMapper.insert(picAttribute);
        }
        catch (Exception e){
            // 如果设置属性失败，应记录错误并考虑是否回滚图片核心信息的插入
            logger.error("设置图片属性失败, pid: {}", picCore.getPid(), e);
            // 根据业务决定是否删除已插入的 picCore 记录
            // picCoreMapper.deleteById(picCore.getPid()); // 比如，如果属性是必须的，则删除
            throw new IOException("图片属性处理失败", e); // 重新抛出异常，让上层处理
        }
    }
}
