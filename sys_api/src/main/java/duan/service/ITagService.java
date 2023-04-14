package duan.service;

import duan.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-14
 */
public interface ITagService extends IService<Tag> {

    void setPicTags(Integer pid, List<String> tags);
}
