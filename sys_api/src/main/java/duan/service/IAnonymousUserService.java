package duan.service;

import duan.entity.AnonymousUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
public interface IAnonymousUserService extends IService<AnonymousUser> {

    void setAnonymousUser(String uuid, HttpServletRequest request);
}
