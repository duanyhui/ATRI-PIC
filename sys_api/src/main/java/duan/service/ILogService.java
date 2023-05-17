package duan.service;

import duan.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    List<Log> getLogList();
}
