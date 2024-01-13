package duan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import duan.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;
import duan.entity.Log_VO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author duanyhui
 * @since 2023-05-05
 */
public interface ILogService extends IService<Log> {

    void setLog(Log log);

    Page<Log_VO> getLogList(Integer pageNum, Integer pageSize);
}
