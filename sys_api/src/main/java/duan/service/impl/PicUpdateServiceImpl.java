package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import duan.entity.AnonymousUser;
import duan.entity.PicUpdate;
import duan.mapper.AnonymousUserMapper;
import duan.mapper.PicUpdateMapper;
import duan.service.IPicUpdateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
@Service
public class PicUpdateServiceImpl extends ServiceImpl<PicUpdateMapper, PicUpdate> implements IPicUpdateService {

    @Autowired
    private PicUpdateMapper picUpdateMapper;
    @Autowired
    private AnonymousUserMapper anonymousUserMapper;
    @Override
    public void setPicUpdate(Integer pid, String uuid,String name) {
        QueryWrapper<AnonymousUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid",uuid);
        //如果没有匿名用户就创建一个
        if(anonymousUserMapper.selectList(queryWrapper).size()<1){
            AnonymousUser anonymousUser = new AnonymousUser();
            anonymousUser.setUuid(uuid);
            anonymousUser.setName(name);
            anonymousUser.setCreateTime(LocalDateTime.now());
            anonymousUserMapper.insert(anonymousUser);
        }
        AnonymousUser anonymousUser = new AnonymousUser();
        anonymousUser.setUuid(uuid);
        anonymousUser.setName(name);
        anonymousUserMapper.update(anonymousUser,queryWrapper);
        PicUpdate picUpdate = new PicUpdate();
        picUpdate.setPid(pid);
        picUpdate.setUuid(uuid);
        picUpdateMapper.insert(picUpdate);
    }
}
