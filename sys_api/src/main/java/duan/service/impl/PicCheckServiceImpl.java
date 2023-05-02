package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import duan.entity.PicCheck;
import duan.entity.PicCore;
import duan.entity.PicDetail_VO;
import duan.mapper.PicCheckMapper;
import duan.mapper.PicCoreMapper;
import duan.service.IPicCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
