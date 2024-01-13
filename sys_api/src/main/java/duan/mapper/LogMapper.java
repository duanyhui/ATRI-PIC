package duan.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import duan.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import duan.entity.Log_VO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author duanyhui
 * @since 2023-05-05
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

    Page<Log_VO> getLogList(Page<Log_VO> page);
}
