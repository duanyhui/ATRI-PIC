package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import duan.common.Result;
import duan.entity.PicAttribute;
import duan.entity.PicCore;
import duan.mapper.PicAttributeMapper;
import duan.mapper.PicCoreMapper;
import duan.service.IPicCoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    @Autowired
    private PicCoreMapper picCoreMapper;
    @Autowired
    private PicAttributeMapper picAttributeMapper;
    @Override
    public Result upload(MultipartFile file, PicCore picCore) throws IOException {
        //生成随机文件名
        String fileName = UploadUtils.getRandomImgName(file.getOriginalFilename());
        //本地上传
        Path path = Paths.get(picPath,fileName);
        Files.write(path,file.getBytes());
//        LambdaQueryWrapper<PicCore> wrapper = new LambdaQueryWrapper<>();
//        PicCore picCore = new PicCore();
        picCore.setFilename(fileName);
        picCore.setLocalurl(path.toString());
        picCoreMapper.insert(picCore);
        LambdaQueryWrapper<PicCore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PicCore::getFilename,fileName);
        picCore = picCoreMapper.selectOne(wrapper);

        PicAttribute picAttribute = new PicAttribute();
        picAttribute.setPid(picCore.getPid());
        picAttribute.setHeight(PicUtils.GetImageHeight(file));
        picAttribute.setWidth(PicUtils.GetImageWidth(file));
        picAttribute.setUpdatetime(LocalDateTime.now());
        picAttributeMapper.insert(picAttribute);
        return Result.succ("上传成功");
    }
}
