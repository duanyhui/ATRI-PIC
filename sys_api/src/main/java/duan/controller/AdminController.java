package duan.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import duan.common.Result;
import duan.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static duan.constant.TokenConstant.ADMIN;

@RestController
//@SaCheckPermission(ADMIN)
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @SaCheckPermission(ADMIN)
    @GetMapping("/test")
    public String test() {
        return "权限测试成功";
    }
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password){
        if(userService.login(username,password)){
            List<String> list = new ArrayList<>();
            list.add("*");
            StpUtil.login(username, SaLoginConfig.setExtra("role",list));
            SaTokenInfo token = StpUtil.getTokenInfo();
            Map<String,String> map = new HashMap<>();
            map.put("token",token.getTokenValue());
            return Result.succ(map);
        }
        return Result.fail("登录失败");
    }

}
