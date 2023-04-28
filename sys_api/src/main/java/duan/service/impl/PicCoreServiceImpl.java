package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import duan.entity.PicAttribute;
import duan.entity.PicCore;
import duan.entity.PicDetail_VO;
import duan.mapper.PicAttributeMapper;
import duan.mapper.PicCoreMapper;
import duan.service.IPicCoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.utils.MapUtils;
import duan.utils.PicUtils;
import duan.utils.UploadUtils;
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


    @Value("${upload.path}")
    private String picPath;
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
        //本地上传
        Path path = Paths.get(picPath,fileName);
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
        picCore.setTags(tagService.getTagsByPid(pid));
        picNumService.addSeeNum(pid);
        return picCore;
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
            throw new IOException("图片处理失败");
        }


    }
}
