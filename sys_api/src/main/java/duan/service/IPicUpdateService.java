package duan.service;

import duan.entity.PicUpdate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
public interface IPicUpdateService extends IService<PicUpdate> {

    void setPicUpdate(Integer pid, String uuid, String name, String mail);
}
