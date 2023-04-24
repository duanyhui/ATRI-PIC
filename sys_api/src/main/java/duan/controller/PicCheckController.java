package duan.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import duan.common.Result;
import duan.service.impl.PicCheckServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import static duan.constant.TokenConstant.ADMIN;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
@RestController
@SaCheckPermission(ADMIN)
@RequestMapping("/picCheck")
public class PicCheckController {
    @Autowired
    private PicCheckServiceImpl picCheckService;
    @GetMapping("/getCheckPicList")
    public Result getCheckPicList(){
        return Result.succ(picCheckService.getCheckPicList());
    }
}

