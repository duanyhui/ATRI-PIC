package duan.mapper;

import duan.entity.PicCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import duan.entity.PicDetail_VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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
}
