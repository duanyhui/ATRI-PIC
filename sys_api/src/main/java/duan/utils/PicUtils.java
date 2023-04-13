package duan.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;


@Component
public class PicUtils {
    private static final Logger logger = LoggerFactory.getLogger(PicUtils.class);
    private static String thumbnailPath;
    private static Double thumbnailQualify;
    @Value("${upload.thumbnail_path}")
    public void setThumbnail_Path(String thumbnail_path) {
        thumbnailPath = thumbnail_path;
    }
    @Value("${upload.thumbnail_quality}")
    public void setThumbnailQualify(Double thumbnailQualify) {
        PicUtils.thumbnailQualify = thumbnailQualify;
    }

    public static int GetImageWidth(MultipartFile file) throws IOException {
        // 从 MultipartFile 中获取字节数组
        byte[] bytes = file.getBytes();
        // 将字节数组转换为 Image 对象
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Image image = ImageIO.read(inputStream);
        return image.getWidth(null);
    }
    public static int GetImageHeight(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Image image = ImageIO.read(inputStream);
        return image.getHeight(null);
    }
    public static int GetImageSize(MultipartFile file) throws IOException {
        long size = file.getSize();
        return (int) size/1024;
    }

    public static void GenerateMiniImage(MultipartFile file,String filename) throws IOException {
        byte[] bytes = file.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Image image = ImageIO.read(inputStream);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        //记录时间
        long start = System.currentTimeMillis();
        // 生成缩略图
        filename = filename.substring(0,filename.lastIndexOf("."));
        Thumbnails.of(file.getInputStream())
                .size(width/2, height/2)
                .outputQuality(thumbnailQualify)
                .outputFormat("jpg")
                .toFile(thumbnailPath+"\\thumbnail_"+filename);
        //计算缩略图大小
        File thumbnailFile = new File(thumbnailPath+"\\thumbnail_"+filename+".jpg");
        int thumbnailSize = (int)thumbnailFile.length()/1024;
        //记录时间
        long end = System.currentTimeMillis();
        logger.info("缩略图生成成功，size:{}KB，用时{}ms",thumbnailSize,(end-start));
    }
}
