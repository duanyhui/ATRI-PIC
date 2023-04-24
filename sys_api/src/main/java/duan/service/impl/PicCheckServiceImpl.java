package duan.service.impl;

import duan.entity.PicCheck;
import duan.entity.PicDetail_VO;
import duan.mapper.PicCheckMapper;
import duan.service.IPicCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
public class PicCheckServiceImpl extends ServiceImpl<PicCheckMapper, PicCheck> implements IPicCheckService {

    @Autowired
    private PicCheckMapper picCheckMapper;
    @Autowired
    private TagServiceImpl tagService;
    @Override
    public List<PicDetail_VO> getCheckPicList() {
        List<Object> list = new ArrayList<>();
        List<PicDetail_VO> checkPicList = picCheckMapper.getCheckPicList();
        for (PicDetail_VO picDetail_vo : checkPicList) {
            List<String> tagList = tagService.getTagsByPid(picDetail_vo.getPid());
            picDetail_vo.setTags(tagList);
        }
        return checkPicList;
    }
}
