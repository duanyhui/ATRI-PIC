package duan.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import duan.common.Result;
import duan.service.impl.PicCoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static duan.constant.TokenConstant.ADMIN;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-14
 */
@RestController
@SaCheckPermission(ADMIN)
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private PicCoreServiceImpl picCoreService;

    @PostMapping("/addPicTag")
    public Result addTag(@RequestParam("pid") Integer pid,
                         @RequestParam("tag") String tag){
        picCoreService.addPicTag(tag,pid);
        return Result.succ("添加成功");
    }
    @PostMapping("/deletePicTag")
    public Result deleteTag(@RequestParam("pid") Integer pid,
                            @RequestParam("tag") String tag){
        picCoreService.deletePicTag(tag,pid);
        return Result.succ("删除成功");
    }



}

