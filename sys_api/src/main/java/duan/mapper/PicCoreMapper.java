package duan.mapper;

import duan.entity.PicCore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import duan.entity.PicDetail_VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-10
 */
@Mapper
public interface PicCoreMapper extends BaseMapper<PicCore> {

    PicDetail_VO selectRandPic();

    PicDetail_VO selectByPid(Integer pid);


    PicDetail_VO selectRandPicByTag(String tag);

    List<PicDetail_VO> getAllPicByTag(String tag);
}
