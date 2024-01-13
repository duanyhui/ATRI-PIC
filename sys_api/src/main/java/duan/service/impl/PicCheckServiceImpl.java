package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.entity.PicCheck;
import duan.entity.PicCore;
import duan.entity.PicDetail_VO;
import duan.entity.PicUpdate;
import duan.mapper.PicCheckMapper;
import duan.mapper.PicCoreMapper;
import duan.mapper.PicUpdateMapper;
import duan.service.IPicCheckService;
import duan.utils.RedisUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
@Service
public class PicCheckServiceImpl extends ServiceImpl<PicCheckMapper, PicCheck> implements IPicCheckService {
    @Value("${upload.temp_path}")
    private String temppicPath;
    @Value("${upload.path}")
    private String picPath;
    @Value("${upload.thumbnail_path}")
    private String thumbPath;
    @Autowired
    private PicCheckMapper picCheckMapper;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private PicCoreMapper picCoreMapper;
    @Autowired
    private PicUpdateMapper picUpdateMapper;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<PicDetail_VO> getCheckPicList() {
        List<Object> list = new ArrayList<>();
        List<PicDetail_VO> checkPicList = picCheckMapper.getCheckPicList();
        for (PicDetail_VO picDetail_vo : checkPicList) {
            List<String> tagList = tagService.getTagsByPid(picDetail_vo.getPid());
            picDetail_vo.setTags(tagList);
        }
        return checkPicList;
    }

    @Override
    public void acceptPic(Integer pid) {
        picCheckMapper.acceptPic(pid);
        //邮件通知
        //将该pid对应的uuid和mail存入redis
        setMailInRedis(pid);


        //将temp文件夹中的图片移动到正式文件夹中
        String fileName = picCoreMapper.selectOne(new QueryWrapper<PicCore>().eq("pid", pid)).getFilename();
        String tempPath = temppicPath + fileName;
        String newPath = picPath + fileName;
        try {
            Files.move(Paths.get(tempPath), Paths.get(newPath));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void forbidPic(Integer pid) {
        picCheckMapper.forbidPic(pid);
    }

    @Override
    public void deletePic(Integer pid) {
        PicCore pic = picCoreMapper.selectOne(new QueryWrapper<PicCore>().eq("pid", pid));
        String fileName = pic.getFilename();
        String status = pic.getStatus();
        if(fileName == null){
            throw new RuntimeException("图片不存在");
        }
        if(status.equals("UNABLE")){
            DeletePic(pid, fileName, temppicPath);
        }
        else if(status.equals("ABLE")){
            DeletePic(pid, fileName, picPath);
        }


    }

    @Override
    public void acceptAllPic() {
        getCheckPicList().forEach(picDetail_vo -> {
            acceptPic(picDetail_vo.getPid());
        });
    }

    @Override
    public void deleteAllPicNotCheck() {
        getCheckPicList().forEach(picDetail_vo -> {
            deletePic(picDetail_vo.getPid());
        });
    }

    @Override
    public Page<PicDetail_VO> getAllPicList(Integer currentPage, Integer pageSize) {
//        List<PicDetail_VO> allPicList = picCheckMapper.getAllPicList();
//        for (PicDetail_VO picDetail_vo : allPicList) {
//            List<String> tagList = tagService.getTagsByPid(picDetail_vo.getPid());
//            picDetail_vo.setTags(tagList);
//        }
//        return allPicList;

        //2024-1-13 重写分页查询
        Page<PicDetail_VO> page = new Page<>(currentPage, pageSize);
        Page<PicDetail_VO> allPicPage = picCheckMapper.getAllPicList(page);
        // 遍历分页结果，为每个PicDetail_VO对象设置标签
        for (PicDetail_VO picDetail_vo : allPicPage.getRecords()) {
            List<String> tagList = tagService.getTagsByPid(picDetail_vo.getPid());
            picDetail_vo.setTags(tagList);
        }
        return allPicPage;
    }

    private void DeletePic(Integer pid, String fileName, String picPath) {
        String Path = picPath + fileName;
        //thumb文件都是以jpg结尾
        String thumbFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";

        String thumb = thumbPath + "thumbnail_" + thumbFileName;
        try {
            Files.delete(Paths.get(Path));
            Files.delete(Paths.get(thumb));
            picCoreMapper.delete(new QueryWrapper<PicCore>().eq("pid", pid));
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException("删除失败");
        }
    }



    /**
     * 将uuid和mail存入redis(key为uuid,value为UserMail对象)
     * @param pid
     */
    void setMailInRedis(Integer pid) {
        LambdaQueryWrapper<PicUpdate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PicUpdate::getPid, pid);
        PicUpdate picUpdate = picUpdateMapper.selectOne(wrapper);
        String uuid = picUpdate.getUuid();
        String mail = picUpdate.getMail();

        if (!mail.equals("")) {
            UserMail userMail;

            // 检查 Redis 中是否已存在该 UUID 的 UserMail
            if (redisUtil.hHasKey("userMails", uuid)) {
                // 获取并更新已存在的 UserMail
                userMail = (UserMail) redisUtil.hget("userMails", uuid);
                userMail.getPids().add(pid);
            } else {
                // 创建新的 UserMail 对象
                userMail = new UserMail();
                userMail.setUuid(uuid);
                userMail.setMail(mail);
                userMail.setPids(new ArrayList<>());
                userMail.getPids().add(pid);
            }

            // 更新 Redis 中的数据
            redisUtil.hset("userMails", uuid, userMail);
        }
    }
    @Data
    public static class UserMail{
        private String uuid;
        private String mail;
        private List<Integer> pids;
    }
}
