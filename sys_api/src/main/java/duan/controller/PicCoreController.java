package duan.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import duan.common.Result;
import duan.entity.PicCore;
import duan.service.impl.PicCheckServiceImpl;
import duan.service.impl.PicCoreServiceImpl;
import duan.service.impl.PicUpdateServiceImpl;
import duan.service.impl.TagServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

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


    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,
                         @RequestParam(value = "info",required = false) String describe,
                         @RequestParam(value = "tags",required = false) List<String> tags,
                         @RequestParam(value = "name",required = false) String author_name,
                         @RequestHeader(value = "satoken",required = false) String token) throws IOException {
        if (token == null) {
            logger.warn("未登录用户上传图片");
            throw new RuntimeException("未登录用户上传图片");
        }
        String uuid = (String) StpUtil.getLoginId();
        String fileType = file.getContentType();
        List<String> allowTypeList = Arrays.asList(allowType.split(","));

        if(!allowTypeList.contains(fileType)){
            logger.warn("文件类型不合法,文件类型为:{},文件名{}",fileType,file.getOriginalFilename());
            throw new RuntimeException("文件类型不合法");
        }
        if(file.isEmpty()||file.getOriginalFilename()==null)
            throw new RuntimeException("文件名不合法");
        PicCore picCore = new PicCore();
        picCore.setInfo(describe);
        if(author_name==null)
            author_name = "夏生";
        picCore.setAuthor(author_name);
        Integer Pid = picCoreService.upload(file, picCore);
        if(tags!=null){
            tagService.setPicTags(Pid,tags);
        }
        picUpdateService.setPicUpdate(Pid,uuid,author_name);




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
    @GetMapping("/rand_pic")
    public Result randPic(@RequestParam(value = "num",required = false,defaultValue = "1") Integer num){
        if(num>10)
            return Result.fail("num不能大于10");
        return Result.succ(picCoreService.getrandPic(num));
    }

    @GetMapping("/get_pic")
    public Result getPic(@RequestParam("pid") Integer pid){
        return Result.succ(picCoreService.getPic(pid));
    }





}

