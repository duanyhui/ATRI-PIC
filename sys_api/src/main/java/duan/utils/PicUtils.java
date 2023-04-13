package duan.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class PicUtils {
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
}
