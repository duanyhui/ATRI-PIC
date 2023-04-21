package duan.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Map;

public class MapUtils {
    public static <T> Map<String, Object> toMapByJson(T obj) {
        // 默认序列化为数字类型的时间戳
        // String jsonStr = JSON.toJSONString(obj);

        // Fastjson内置了一个默认的日期格式yyyy-MM-dd HH:mm:ss，
        // 可以通过在调用JSON.toJSONString时传入SerializerFeature.WriteDateUseDateFormat来启用。
        // 通过修改默认的时间格式，结合启用默认日期格式，也可以达到按指定日期格式序列化的目的
        // JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
        String jsonStr = JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);

        Map<String, Object> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
        });
        return map;
    }


}
