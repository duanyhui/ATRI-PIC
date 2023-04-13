package duan.controller;


import duan.common.Result;
import duan.entity.PicCore;
import duan.service.impl.PicCoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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

    @Autowired
    private PicCoreServiceImpl picCoreService;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,
                         @RequestParam(value = "describe",required = false) String describe,
                         @RequestParam(value = "tags",required = false) List<String> tags) throws IOException {
        String fileType = file.getContentType();
        List<String> allowTypeList = Arrays.asList(allowType.split(","));

        if(!allowTypeList.contains(fileType))
            throw new RuntimeException("文件类型不合法");
        if(file.isEmpty()||file.getOriginalFilename()==null)
            throw new RuntimeException("文件名不合法");
        PicCore picCore = new PicCore();
        picCore.setInfo(describe);
        picCoreService.upload(file,picCore);


        return Result.succ("上传成功");
    }
}

