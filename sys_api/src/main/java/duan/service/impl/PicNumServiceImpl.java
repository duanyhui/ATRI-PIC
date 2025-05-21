package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.entity.PicNum;
import duan.mapper.PicNumMapper;
import duan.service.IPicNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-26
 */
@Service
public class PicNumServiceImpl extends ServiceImpl<PicNumMapper, PicNum> implements IPicNumService {

    @Autowired
    private PicNumMapper picNumMapper;
    @Autowired
    private PicRankService picRankService;
    @Override
    @Async
    public void setPicNum(Integer pid) {
        PicNum picNum = new PicNum();
        picNum.setPid(pid);
        picNumMapper.insert(picNum);

    }

    @Override
    public void vote(Integer pid, Integer vote) {
        LambdaQueryWrapper<PicNum> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PicNum::getPid, pid);
        PicNum picNum = picNumMapper.selectOne(wrapper);
        if(picNum == null){
            throw new RuntimeException("图片不存在");
        }
        if (vote == 1) {
            picNum.setLikenum(picNum.getLikenum() + 1);
            picRankService.likePic(pid);
        } else if (vote == -1){
            picNum.setUnlikenum(picNum.getUnlikenum() + 1);
            picRankService.unlikePic(pid);
        }
        else {
            picNum.setLoadnum(picNum.getLoadnum() + 1);
        }
        picNumMapper.update(picNum, wrapper);
    }

    @Override
    public void addSeeNum(Integer pid) {
        picNumMapper.addSeeNum(pid);
        picRankService.seePic(pid);
    }
    @Override
    public List<PicNum> getAllPicNumList() {
        LambdaQueryWrapper<PicNum> wrapper = new LambdaQueryWrapper<>();
        //获取所有的图片
        return picNumMapper.selectList(wrapper);
    }

}
