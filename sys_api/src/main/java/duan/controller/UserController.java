package duan.controller;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import duan.common.Result;
import duan.entity.User;
import duan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/send-email-code")
    public Result sendEmailCode(@RequestParam String email) {
        String message = userService.sendEmailCode(email);
        if ("验证码发送成功".equals(message)) {
            return Result.succ(message);
        }
        return Result.fail(message);
    }

    @PostMapping("/register")
    public Result registerByEmail(@RequestParam String username,
                                  @RequestParam String pwd,
                                    @RequestParam String email,
                                  @RequestParam String code) {
        if (username.isEmpty() || pwd.isEmpty() || email.isEmpty()) {
            return Result.fail("用户名、密码和邮箱不能为空");
        }
        User user = new User();
        user.setUsername(username);
        user.setPwd(pwd);
        user.setEmail(email);
        boolean success = userService.registerByEmail(user, code);
        if (success) {
            // 注册成功后，使用Sa-Token生成token并返回
            StpUtil.login(user.getUid());
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return Result.succ(tokenInfo);
        }
        return Result.fail("注册失败，验证码错误或邮箱已注册");
    }
}
