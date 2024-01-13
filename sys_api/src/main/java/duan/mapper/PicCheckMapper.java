package duan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import duan.entity.PicCheck;
import duan.entity.PicDetail_VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
@Mapper
public interface PicCheckMapper extends BaseMapper<PicCheck> {

    List<PicDetail_VO> getCheckPicList();

    void acceptPic(Integer pid);

    void forbidPic(Integer pid);

//    List<PicDetail_VO> getAllPicList();
    Page<PicDetail_VO> getAllPicList(Page<?> page);
}
