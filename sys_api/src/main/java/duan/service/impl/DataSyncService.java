package duan.service.impl;

import duan.entity.PicNum;
import duan.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DataSyncService {

    @Autowired
    private PicNumServiceImpl picNumService; // 用于查询数据库
    @Autowired
    private RedisUtil redisUtil; // Redis 工具类

    private static final String PIC_LIKE_RANK = "pic:like:rank";
    private static final String PIC_SEE_RANK = "pic:see:rank";

    @PostConstruct
    public void init() {
        syncRankNumToRedisAsync(); // 调用异步方法
    }
    @Async
    public void syncRankNumToRedisAsync() {
        // 从数据库中查询数据
        List<PicNum> dataList = picNumService.getAllPicNumList();
        if (dataList == null || dataList.isEmpty()) {
            System.out.println("No data to sync.");
            return;
        }
        // 清空 Redis 中的旧数据
        redisUtil.del(PIC_LIKE_RANK);
        redisUtil.del(PIC_SEE_RANK);


        // 将数据同步到 Redis
        for (PicNum data : dataList) {
            redisUtil.Zadd(PIC_LIKE_RANK, data.getPid().toString(), data.getLikenum());
            redisUtil.Zadd(PIC_SEE_RANK, data.getPid().toString(), data.getSeenum());
        }
        System.out.println("------------------------------------------");
        System.out.println("同步点赞数和浏览数到 Redis 完成");
        System.out.println("------------------------------------------");
    }
}