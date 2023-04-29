package duan.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import duan.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static duan.constant.TokenConstant.USER_UPLOAD;

@RestController
@RequestMapping("/utils")
public class UtilsController {
    @GetMapping("/uuid")
    public Result getUuid() {
        String uuid = java.util.UUID.randomUUID().toString();
        return Result.succ(uuid);
    }

    /**
     * 生成一个具有访客权限的token和uuid
     * @param
     * @return
     */
    @GetMapping("/token")
    public Result getToken() {
        String uuid = java.util.UUID.randomUUID().toString();
        List<String> list = new ArrayList<>();
        list.add(USER_UPLOAD);
        list.add("test");
        StpUtil.login(uuid,
                SaLoginConfig.setExtra("role",list));
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        Map<String,String> map = new HashMap<>();
        map.put("token",tokenInfo.getTokenValue());
        map.put("uuid",uuid);
        List<String> role = (List<String>) StpUtil.getExtra(tokenInfo.getTokenValue(), "role");
        List<String> roleList = StpUtil.getPermissionList(uuid);
        boolean b = StpUtil.hasPermission(uuid, USER_UPLOAD);
        return Result.succ(map);
    }
    @GetMapping("/hasPermission")
    @SaCheckPermission(USER_UPLOAD)
public Result hasPermission(@RequestParam("satoken") String token) {
        return Result.succ(StpUtil.hasPermission(token, USER_UPLOAD));
    }

}
