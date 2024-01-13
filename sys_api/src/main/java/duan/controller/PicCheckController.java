package duan.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import duan.common.Result;
import duan.service.impl.PicCheckServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/getAllPicList")
    public Result getAllPicList(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize){
        return Result.succ(picCheckService.getAllPicList(pageNum,pageSize));
    }
    @PostMapping("/accept")
    public Result checkPic(@RequestBody List<Integer> Pid){
        for (Integer pid : Pid) {
            picCheckService.acceptPic(pid);
        }
        return Result.succ("审核通过");
    }
    @PostMapping("/acceptAll")
    public Result checkAllPic(){
        picCheckService.acceptAllPic();
        return Result.succ("全部审核通过");
    }
    @PostMapping("/forbid")
    public Result rejectPic(@RequestBody List<Integer> Pid){
        for (Integer pid : Pid) {
            picCheckService.forbidPic(pid);
        }
        return Result.succ("审核拒绝成功");
    }
    @PostMapping("/delete")
    public Result deletePic(@RequestBody() List<Integer> Pid){
        for (Integer pid : Pid) {
            picCheckService.deletePic(pid);
        }
        return Result.succ("删除成功");
    }

    @PostMapping("/deleteAll")
    public Result deleteAllPic(){
        picCheckService.deleteAllPicNotCheck();
        return Result.succ("全部删除成功");
    }

}

