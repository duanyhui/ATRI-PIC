package duan.service;

import duan.entity.PicNum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-26
 */
public interface IPicNumService extends IService<PicNum> {

    void setPicNum(Integer pid);


    void vote(Integer pid, Integer vote);

    void addSeeNum(Integer pid);

    List<PicNum> getAllPicNumList();
}
