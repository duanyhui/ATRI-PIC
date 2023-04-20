package duan.service.impl;

import duan.entity.AnonymousUser;
import duan.mapper.AnonymousUserMapper;
import duan.service.IAnonymousUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
@Service
public class AnonymousUserServiceImpl extends ServiceImpl<AnonymousUserMapper, AnonymousUser> implements IAnonymousUserService {

}
