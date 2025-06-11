package duan.task;

import duan.entity.PicDetail_VO;
import duan.service.impl.PicCoreServiceImpl;
import duan.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RandPicCacheTask {
    @Autowired
    private PicCoreServiceImpl picCoreService;
    @Autowired
    private RedisUtil redisUtil;
    private static final String RAND_PIC_KEY = "rand:pic:list";
    private static final int CACHE_SIZE = 200; // 缓存图片数量
    private static final int CACHE_EXPIRE = 300; // 缓存有效期（秒）

    // 每5分钟刷新一次缓存
    @Scheduled(fixedRate = 5*60*1000)
    public void cacheRandPics() {
        List<PicDetail_VO> randPicList = picCoreService.getrandPicFromDb(CACHE_SIZE);
        redisUtil.del(RAND_PIC_KEY); // 清空旧缓存
        for (PicDetail_VO pic : randPicList) {
            // 将每个图片对象转换为JSON字符串存储
            redisUtil.lSet(RAND_PIC_KEY, pic, CACHE_EXPIRE);
        }
//        redisUtil.lSet(RAND_PIC_KEY, randPicList, CACHE_EXPIRE);
    }

}
