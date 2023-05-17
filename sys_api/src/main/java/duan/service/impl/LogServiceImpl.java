package duan.service.impl;

import duan.entity.Log;
import duan.mapper.LogMapper;
import duan.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duanyhui
 * @since 2023-05-05
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Autowired
    private LogMapper logMapper;
    @Override
    public void setLog(Log log) {
        logMapper.insert(log);
    }

    @Override
    public List<Log> getLogList() {
        return logMapper.selectList(null);
    }
}
