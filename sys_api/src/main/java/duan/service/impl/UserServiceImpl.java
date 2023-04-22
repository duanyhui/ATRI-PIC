package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import duan.entity.User;
import duan.mapper.UserMapper;
import duan.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        String pwd = userMapper.selectOne(wrapper).getPwd();
        if(HashUtils.MatchHash(password,pwd))
            return true;
        else
            return false;

    }
}
