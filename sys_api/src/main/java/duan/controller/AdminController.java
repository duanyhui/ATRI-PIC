package duan.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import duan.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static duan.constant.TokenConstant.ADMIN;

@RestController
@SaCheckPermission(ADMIN)
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/test")
    public String test() {
        return "权限测试成功";
    }
    @PostMapping("/login")
    public Result login() {
        return Result.succ("登录成功");
    }

}
