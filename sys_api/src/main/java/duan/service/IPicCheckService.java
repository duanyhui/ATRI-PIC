package duan.service;

import duan.entity.PicCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import duan.entity.PicDetail_VO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
public interface IPicCheckService extends IService<PicCheck> {

    List<PicDetail_VO> getCheckPicList();

    void acceptPic(Integer pid);

    void forbidPic(Integer pid);

    void deletePic(Integer pid);

    void acceptAllPic();
}
