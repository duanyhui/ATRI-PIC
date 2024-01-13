package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.entity.Log;
import duan.mapper.LogMapper;
import duan.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Page<Log> getLogList(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Log> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Log::getId);
        //输出递减的日志
        Page<Log> page = new Page<>(pageNum,pageSize);
        return logMapper.selectPage(page,queryWrapper);
    }
}
