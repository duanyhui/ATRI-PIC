package duan.mapper;

import duan.entity.PicCore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-10
 */
@Mapper
public interface PicCoreMapper extends BaseMapper<PicCore> {

    PicCore selectRandPic();
}
