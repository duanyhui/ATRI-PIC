package duan.service;

import duan.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-10
 */
public interface IUserService extends IService<User> {

    boolean login(String username, String password);

    // 发送邮箱验证码
    String sendEmailCode(String email);

    // 校验邮箱验证码并注册用户
    boolean registerByEmail(User user, String code);

    // 根据邮箱查询用户
    User getUserByEmail(String email);
}
