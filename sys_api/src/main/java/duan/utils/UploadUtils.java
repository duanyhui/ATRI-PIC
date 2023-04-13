package duan.utils;

import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UploadUtils {
    /**
     * @Description: 生成唯一图片名称
     * @Param: fileName
     * @return: 云服务器fileName
     */
    public static String getRandomImgName(String fileName) {
        int index = fileName.lastIndexOf(".");

        if (fileName.isEmpty() || index == -1){
            throw new IllegalArgumentException("文件名不合法");
        }
        // 获取文件后缀
        String suffix = fileName.substring(index).toLowerCase();
        // 生成UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //生成当前时间
        String time = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        // 拼接新的名称
        return time + "-" + uuid + suffix;
    }

public static void main(String[] args) {
        System.out.println(getRandomImgName("test.jpg"));
    }

    public static void uploadToLocal(MultipartFile file, String fileName) {


    }
}
