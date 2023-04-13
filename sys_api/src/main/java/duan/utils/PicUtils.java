package duan.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PicUtils {
    public static int GetImageWidth(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read((File) file);
        return image.getWidth();
    }
    public static int GetImageHeight(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read((File) file);
        return image.getHeight();
    }
}
