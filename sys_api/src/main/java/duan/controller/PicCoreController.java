package duan.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import duan.common.Result;
import duan.entity.PicCore;
import duan.entity.PicDetail_VO;
import duan.service.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static duan.constant.TokenConstant.USER_UPLOAD;

/**
 * <p>
 *  图片操作相关api
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/pic")
public class PicCoreController {
    @Value("${upload.path}")
    private String picPath;
    @Value("${upload.allow_type}")
    private String allowType;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PicCoreServiceImpl picCoreService;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private PicUpdateServiceImpl picUpdateService;
    @Autowired
    private PicCheckServiceImpl picCheckService;
    @Autowired
    private PicNumServiceImpl picNumService;


    @SaCheckPermission(USER_UPLOAD)
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") List<MultipartFile> files,
                         @RequestParam(value = "info",required = false) String describe,
                         @RequestParam(value = "tags",required = false) List<String> tags,
                         @RequestParam(value = "author",required = false) String author_name,
                         @RequestHeader(value = "satoken",required = false) String token) throws IOException {
        if (token == null) {
            logger.warn("未登录用户上传图片");
            throw new RuntimeException("未登录用户上传图片");
        }
        String uuid = (String) StpUtil.getLoginId();
        for(MultipartFile file:files) {
            //获取文件类型(后缀
            String fileType = file.getContentType();
            List<String> allowTypeList = Arrays.asList(allowType.split(","));

            if (!allowTypeList.contains(fileType)) {
                logger.warn("文件类型不合法,文件类型为:{},文件名{}", fileType, file.getOriginalFilename());
                throw new RuntimeException("文件类型不合法");
            }
            if (file.isEmpty() || file.getOriginalFilename() == null)
                throw new RuntimeException("文件名不合法");
            PicCore picCore = new PicCore();
            picCore.setInfo(describe);
            if (author_name == null)
                author_name = "夏生";
            picCore.setAuthor(author_name);
            Integer Pid = picCoreService.upload(file, picCore);
            if (tags != null) {
                tagService.setPicTags(Pid, tags);
            }
            picUpdateService.setPicUpdate(Pid, uuid, author_name);
            picNumService.setPicNum(Pid);
        }



        return Result.succ("上传成功");
    }

    @GetMapping("/testPermission")
    @SaCheckPermission(USER_UPLOAD)
    public Result testPermission(){
        return Result.succ("权限测试成功");
    }

    /**
     * 获取随机已审核图片
     * @param num
     * @return
     */
    @GetMapping("/rand")
    public Result randPic(@RequestParam(value = "num",required = false,defaultValue = "1") Integer num){
        if(num>20)
            return Result.fail("num不能大于20");
        return Result.succ(picCoreService.getrandPic(num));
    }

    @GetMapping("/get")
    public Result getPic(@PathParam("pid") Integer pid){
        return Result.succ(picCoreService.getPic(pid));
    }

    @PostMapping("/vote")
    public Result vote(@RequestParam("pid") Integer pid,
                       @RequestParam("vote") Integer vote){
        // 不可冲投票或者取消投票
        if(vote!=1&&vote!=-1&&vote!=0)
            return Result.fail("投票失败");
        picNumService.vote(pid,vote);
        return Result.succ("投票成功");
    }
    @GetMapping("/getByTag")
    public Result getByTag(@RequestParam("tag") String tag,
                           @RequestParam(value = "num",required = false,defaultValue = "1") Integer num){
        if(num>30)
            return Result.fail("请求图片数量过多");
        List<PicDetail_VO> picDetail_vos = picCoreService.getByTag(tag,num);
        return Result.succ(picDetail_vos);
    }




}

