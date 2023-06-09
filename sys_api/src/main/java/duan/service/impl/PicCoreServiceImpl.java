package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import duan.common.Result;
import duan.entity.*;
import duan.mapper.PicAttributeMapper;
import duan.mapper.PicCoreMapper;
import duan.mapper.TagMapper;
import duan.service.IPicCoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.utils.MapUtils;
import duan.utils.PicUtils;
import duan.utils.UploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<PicDetail_VO> getrandPic(Integer num) {
        List<PicDetail_VO> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            //随机获取一张图片
            PicDetail_VO picCore = picCoreMapper.selectRandPic();

//            map = MapUtils.toMapByJson(picCore);
            //获取图片标签
//            map.put("tags",tagService.getTagsByPid(picCore.getPid()));
//            list.add(map);
//            list.add(picCoreMapper.selectRandPic());
              picCore.setTags(tagService.getTagsByPid(picCore.getPid()));
              list.add(picCore);

        }
        return list;
    }

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
            throw new RuntimeException("标签不存在");
        }
        // 如果请求的图片数量大于数据库中的图片数量，则返回数据库中的所有图片
        if(count<num){
            list=picCoreMapper.getAllPicByTag(tag);
            for(PicDetail_VO picCore:list){
                picCore.setTags(tagService.getTagsByPid(picCore.getPid()));
            }
            return list;
        }
        for (int i = 0; i < num; i++) {
            //随机获取一张图片
            PicDetail_VO picCore = picCoreMapper.selectRandPicByTag(tag);
            if(picCore == null){
                throw new RuntimeException("图片不存在");
            }
            picCore.setTags(tagService.getTagsByPid(picCore.getPid()));
            list.add(picCore);
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
        if(tagService.getOne(wrapper)==null){
            //标签不存在则创建标签
            Tag tag1 = new Tag();
            tag1.setTagName(tag);
            tagService.save(tag1);
        }
        //获取标签id
        Integer tagId = tagService.getOne(wrapper).getId();
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
        if(tagService.getOne(wrapper)==null){
            throw new RuntimeException("标签不存在");
        }
        //获取标签id
        Integer tagId = tagService.getOne(wrapper).getId();
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
            picCoreMapper.deleteById(picCore.getPid());
            logger.error("图片处理失败");
            throw new IOException("图片处理失败");
        }


    }

}
