package duan.mapper;

import duan.entity.PicNum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-26
 */
@Mapper
public interface PicNumMapper extends BaseMapper<PicNum> {

    void addSeeNum(Integer pid);
}
