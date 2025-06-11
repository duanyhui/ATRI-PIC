package duan.service.impl;

import duan.entity.PicDetail_VO;
import duan.service.IPicRankService;
import duan.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class PicRankService implements IPicRankService {

    private static final String PIC_LIKE_RANK = "pic:like:rank";
    private static final String PIC_SEE_RANK = "pic:see:rank";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    @Lazy
    private PicCoreServiceImpl picCoreService;

    // 点赞时调用
    public void likePic(Integer picId) {
        redisUtil.zIncrScore(PIC_LIKE_RANK, picId.toString(), 1);
    }
    // 浏览时调用
    public void seePic(Integer picId) {
        redisUtil.zIncrScore(PIC_SEE_RANK, picId.toString(), 1);
    }

    // 取消点赞时调用
    public void unlikePic(Integer picId) {
        redisUtil.zIncrScore(PIC_LIKE_RANK, picId.toString(), -1);
    }

    // 获取喜欢排行榜前N名的图片详细信息
    public List<PicDetail_VO> getTopLikes(int topN) {
        Set<Object> objects = redisUtil.zGetTop(PIC_LIKE_RANK, 0, topN - 1);
        // 这里需要将 Set<Object> 中的value转换为Integer,然后通过这个Integer去数据库中查询图片的详细信息

        List<PicDetail_VO> picDetails = new ArrayList<>();
        for (Object object : objects) {
            Integer picId = Integer.valueOf(object.toString());

            PicDetail_VO picDetail = picCoreService.getPic(picId);
            picDetails.add(picDetail);
        }
        return picDetails;
    }
    // 获取浏览排行榜前N名的图片详细信息
    public List<PicDetail_VO> getTopSees(int topN) {
        Set<Object> objects = redisUtil.zGetTop(PIC_SEE_RANK, 0, topN - 1);
        // 这里需要将 Set<Object> 中的value转换为Integer,然后通过这个Integer去数据库中查询图片的详细信息

        List<PicDetail_VO> picDetails = new ArrayList<>();
        for (Object object : objects) {
            Integer picId = Integer.valueOf(object.toString());

            PicDetail_VO picDetail = picCoreService.getPic(picId);
            picDetails.add(picDetail);
        }
        return picDetails;
    }

    // 获取某图片当前排名
    public Long getPicRank(Integer picId) {
        return redisUtil.zGetRank(PIC_LIKE_RANK, picId.toString());
    }

    // 获取某图片当前分数
    public Double getPicScore(Integer picId) {

        return redisUtil.zGetScore(PIC_LIKE_RANK, picId.toString());
    }
    public void addPicRank (Integer Pid) {
        redisUtil.Zadd(PIC_LIKE_RANK, Pid.toString(), 0);
        redisUtil.Zadd(PIC_SEE_RANK, Pid.toString(), 0);
    }
    public void deletePicRank (Integer Pid) {
        redisUtil.Zdel(PIC_LIKE_RANK, Pid.toString());
        redisUtil.Zdel(PIC_SEE_RANK, Pid.toString());
    }
}