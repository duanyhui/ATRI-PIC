package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import duan.entity.AnonymousUser;
import duan.mapper.AnonymousUserMapper;
import duan.service.IAnonymousUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import duan.utils.IpAddressUtils;
import duan.utils.UserAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

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

    @Autowired
    private AnonymousUserMapper anonymousUserMapper;

    @Override
    @Async
    public void setAnonymousUser(String uuid, HttpServletRequest request) {
        QueryWrapper<AnonymousUser> queryWrapper = new QueryWrapper<>();
        String ip = IpAddressUtils.getIpAddress(request);
        String address = IpAddressUtils.getCityInfo(ip);
        String UserAgent = request.getHeader("User-Agent");
        UserAgentUtils userAgentUtils = new UserAgentUtils();
        Map<String, String> osAndBrower = userAgentUtils.parseOsAndBrowser(UserAgent);
          String os = osAndBrower.get("os");
        String browser = osAndBrower.get("browser");
        queryWrapper.eq("uuid",uuid);
        //如果没有匿名用户就创建一个
        if(anonymousUserMapper.selectList(queryWrapper).size()<1){
            AnonymousUser anonymousUser = new AnonymousUser();
            anonymousUser.setUuid(uuid);
            anonymousUser.setName("匿名用户");
            anonymousUser.setIp(ip);
            anonymousUser.setAddress(address);
            anonymousUser.setOs(os);
            anonymousUser.setBrowser(browser);
            anonymousUser.setCreateTime(LocalDateTime.now());
            anonymousUserMapper.insert(anonymousUser);
        }
    }

    @Override
    public Page<AnonymousUser> getUserInfoList(Integer pageNum, Integer pageSize) {
        //按照id倒序
        QueryWrapper<AnonymousUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("uid");
        Page<AnonymousUser> page = new Page<>(pageNum,pageSize);
        return anonymousUserMapper.selectPage(page,queryWrapper);
    }
}
