package duan.utils;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import duan.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;



@Component
public class QiniuUtils {
    private static String DOMAIN_NAME;
    private static String ACCESS_KEY;
    private static String SECRET_KEY;
    private static String BUCKET_NAME;
    private static String documentName;
    @Value("${qiniu.domain}")
    public void setDOMAIN_NAME(String DOMAIN_NAME) {
        this.DOMAIN_NAME = DOMAIN_NAME;
    }
    @Value("${qiniu.access_key}")
    public void setACCESS_KEY(String ACCESS_KEY) {
        this.ACCESS_KEY = ACCESS_KEY;
    }
    @Value("${qiniu.secret_key}")
    public void setSECRET_KEY(String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }
    @Value("${qiniu.bucket}")
    public void setBUCKET_NAME(String BUCKET_NAME) {
        this.BUCKET_NAME = BUCKET_NAME;
    }
    @Value("${qiniu.documentName}")
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
    /**
     * 上传文件
     * @param file
     * @return
     */
    public static Result upload(MultipartFile file){
        if (file.isEmpty()||file.getOriginalFilename()!=null) {
            return Result.fail("上传失败，请选择文件");
        }
        // 生成文件名
        String fileName = UploadUtils.getRandomImgName(file.getOriginalFilename());
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huadong());  //根据自己的对象空间的地址选（华东）
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKET_NAME);
            Response response = uploadManager.put(uploadBytes, documentName+fileName , upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return Result.succ(DOMAIN_NAME+documentName+fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
          return Result.fail("上传失败");
    }


    /**
     * 删除文件
     * @param key 文件在七牛云上的唯一标识符
     * @return
     */
    public static Result delete(String key){
        Configuration cfg = new Configuration(Region.huadong());
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        if (!exists(key)) return Result.fail("文件不存在");
        try {
            key = documentName+key;
            Response response = bucketManager.delete(BUCKET_NAME, key);
            return Result.succ("删除成功");
        } catch (QiniuException ex) {
            ex.printStackTrace();
            Response r = ex.response;
            return Result.fail("删除失败: " + r.toString());
        }
    }
    public static boolean exists(String key){
        Configuration cfg = new Configuration(Region.huadong());
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(BUCKET_NAME, documentName+key);
            return true;
        } catch (QiniuException ex) {
            if (ex.code() == 404) {
                // 如果是 404 错误，则表示文件不存在
                return false;
            } else {
                // 其他错误则打印异常信息并返回 false
                ex.printStackTrace();
                return false;
            }
        }
    }

}
