package duan.service;

import duan.entity.PicCore;
import com.baomidou.mybatisplus.extension.service.IService;
import duan.entity.PicDetail_VO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-10
 */
public interface IPicCoreService extends IService<PicCore> {

    Integer upload(MultipartFile file, PicCore picCore) throws IOException;

    List<PicDetail_VO> getrandPic(Integer num);

    PicDetail_VO getPic(Integer pid);


    List<PicDetail_VO> getByTag(String tag, Integer num);

    void addPicTag(String tag, Integer pid);

    void deletePicTag(String tag, Integer pid);

    Long getPicCount();
}
