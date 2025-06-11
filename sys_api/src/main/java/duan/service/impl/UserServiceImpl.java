package duan.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.constant.UserRoleConstant;
import duan.entity.User;
import duan.mapper.UserMapper;
import duan.service.IUserService;
import duan.utils.HashUtils;
import duan.utils.RedisUtil;
import duan.utils.SendMailUtil;
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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SendMailUtil sendMailUtil;

    private static final String EMAIL_CODE_KEY_PREFIX = "email:code:";
    private static final long EMAIL_CODE_EXPIRE_SECONDS = 300; // 验证码5分钟有效

    @Override
    public boolean adminLogin(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        if(userMapper.selectOne(wrapper)==null)
            return false;
        if(!userMapper.selectOne(wrapper).getRole().equals(UserRoleConstant.ADMIN))
            return false; // 不是管理员角色
        String pwd = userMapper.selectOne(wrapper).getPwd();
        if(HashUtils.MatchHash(password,pwd))
            return true;
        else
            return false;

    }
    @Override
    public boolean userLogin(String email, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(User::getEmail,email);
        if(userMapper.selectOne(wrapper)==null)
            return false;
        if(!userMapper.selectOne(wrapper).getRole().equals(UserRoleConstant.USER))
            return false; // 不是普通用户角色
        String pwd = userMapper.selectOne(wrapper).getPwd();
        if(HashUtils.MatchHash(password,pwd))
            return true;
        else
            return false;

    }

    @Override
    public String sendEmailCode(String email) {
        // 检查邮箱是否已被注册
        if (getUserByEmail(email) != null) {
            return "该邮箱已被注册";
        }
        // 生成6位随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        // 将验证码存入Redis,并设置有效期
        redisUtil.set(EMAIL_CODE_KEY_PREFIX + email, code, EMAIL_CODE_EXPIRE_SECONDS);
        // 发送邮件
        try {
            // 注意：SendMailUtil中的sendMail方法可能需要调整以适应发送验证码的邮件模板
            // 这里假设SendMailUtil有一个通用的发送文本邮件的方法或可以改造
            // 此处仅为示例，您需要根据您的SendMailUtil进行调整
            sendMailUtil.sendRegisterMail(Integer.valueOf(code), email);
            return "验证码发送成功";
        } catch (Exception e) {
            log.error("发送邮箱验证码失败: {}", e);
            return "验证码发送失败，请稍后重试";
        }
    }

    @Override
    public boolean registerByEmail(User user, String code) {
        String email = user.getEmail();
        // 从Redis中获取验证码
        String redisCode = (String) redisUtil.get(EMAIL_CODE_KEY_PREFIX + email);
        if (redisCode == null) {
            return false; // 验证码不存在或已过期
        }
        if (!redisCode.equals(code)) {
            return false; // 验证码不正确
        }
        // 验证通过，对密码进行哈希处理
        user.setPwd(SaSecureUtil.sha256(user.getPwd()));
        user.setIsEmailVerified(true);
        user.setRole("ROLE_USER"); // 默认角色
        user.setStatus("ABLE"); // 默认状态
        user.setUuid(java.util.UUID.randomUUID().toString()); // 生成UUID
        // 保存用户
        boolean success = save(user);
        if (success) {
            // 注册成功后删除Redis中的验证码
            redisUtil.del(EMAIL_CODE_KEY_PREFIX + email);
        }
        return success;
    }

    @Override
    public User getUserByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        return userMapper.selectOne(wrapper);
    }
}
