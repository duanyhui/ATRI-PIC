package duan.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import duan.common.Result;
import duan.entity.Log;
import duan.handler.HeaderInterceptor;
import duan.mapper.AboutMapper;
import duan.service.impl.AnonymousUserServiceImpl;
import duan.service.impl.LogServiceImpl;
import duan.service.impl.PicCoreServiceImpl;
import duan.utils.IpAddressUtils;
import duan.utils.IpUtil;
import duan.utils.LogUtils;
import duan.utils.PicUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static duan.constant.TokenConstant.USER_UPLOAD;

@RestController
@RequestMapping("/utils")
public class UtilsController {
    private static final Logger logger = LoggerFactory.getLogger(UtilsController.class);
    @Autowired
    private AboutMapper aboutMapper;
    @Autowired
    private AnonymousUserServiceImpl anonymousUserService;
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private PicCoreServiceImpl picCoreService;

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
    public Result getToken(HttpServletRequest request) {
        String uuid = java.util.UUID.randomUUID().toString();
        List<String> list = new ArrayList<>();
        list.add(USER_UPLOAD);
        list.add("test");
        StpUtil.login(uuid,
                SaLoginConfig.setExtra("role",list).setExtra("uuid",uuid));
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        Map<String,String> map = new HashMap<>();
        map.put("token",tokenInfo.getTokenValue());
        map.put("uuid",uuid);
        anonymousUserService.setAnonymousUser(uuid, request);
        return Result.succ(map);
    }
    @GetMapping("/hasPermission")
    @SaCheckPermission(USER_UPLOAD)
public Result hasPermission(@RequestParam("satoken") String token) {
        return Result.succ(StpUtil.hasPermission(token, USER_UPLOAD));
    }

    @GetMapping("/about")
    public Result about() {
        logger.info("访问/about");
        logUtils.urlLog("/about", HeaderInterceptor.getSatoken());
        return Result.succ(aboutMapper.selectOne(null).getAbout());
    }

    @GetMapping("/test")
    public Result test(HttpServletRequest request) {
        return Result.succ(IpUtil.getIpAddr(request));
    }

    @PostMapping("/setUrlLog")
    public Result setInfo(@RequestParam("url") String url) {
        logUtils.urlLog(url, HeaderInterceptor.getSatoken());
        return Result.succ("操作成功");
    }

    @GetMapping("/getPicCount")
    public Result getPicCount(){
        return Result.succ(picCoreService.getPicCount());
    }



}
