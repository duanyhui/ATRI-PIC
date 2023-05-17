package duan.utils;

import cn.dev33.satoken.stp.StpUtil;
import duan.entity.Log;
import duan.service.impl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LogUtils {
    @Autowired
    private  LogServiceImpl logService;// todo 为什么不能注入
    @Async
    public void urlLog(String url,String token){
        Log log = new Log();
        log.setLog("访问"+url);
        log.setUrl(url);
        log.setUuid(GetUuid(token));
        log.setTime(LocalDateTime.now());
        log.setType("URL");
        logService.setLog(log);
    }
    @Async
    public void VoteLog(Integer vote,Integer pid,String token){
        Log log = new Log();
        if(vote==1){
            log.setLog("点赞pid："+pid);
        }else if(vote==-1){
            log.setLog("点踩pid："+pid);
        }
        else if(vote==0){
            log.setLog("下载pid："+pid+"的投票");
        }
        log.setUuid(GetUuid(token));
        log.setTime(LocalDateTime.now());
        log.setType("VOTE");
        logService.setLog(log);
    }
    private static String GetUuid(String token){
        return (String) StpUtil.getExtra(token,"uuid");
    }

    @Async
    public void uploadLog(Integer pid, String satoken) {
        Log log = new Log();
        log.setLog("上传pid："+pid);
        log.setUuid(GetUuid(satoken));
        log.setTime(LocalDateTime.now());
        log.setType("UPLOAD");
        logService.setLog(log);
    }
}
