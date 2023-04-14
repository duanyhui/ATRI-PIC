package duan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import duan.entity.PicTag;
import duan.entity.Tag;
import duan.mapper.PicTagMapper;
import duan.mapper.TagMapper;
import duan.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-14
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private PicTagMapper picTagMapper;
    @Override
    public void setPicTags(Integer pid, List<String> tags) {
        // todo 放到Redis中加快查询速度
        List<Tag> exist_tags = tagMapper.selectList(null);
        List<String> tagNames = exist_tags.stream().map(Tag::getTagName).collect(Collectors.toList());
        LambdaQueryWrapper<PicTag> picTagWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Tag> tagWrapper = new LambdaQueryWrapper<>();
        PicTag picTag = new PicTag();
            for(String tag:tags){
                // 如果存在则插入pic_tag表
                if(tagNames.contains(tag)) {
                    tagWrapper.clear();
                    Tag exist_tag = tagMapper.selectList(tagWrapper.eq(Tag::getTagName,tag)).get(0);
                    picTag.setTagId(exist_tag.getId());
                }
                // 如果不存在则插入tag表和pic_tag表
                else {
                    Tag new_tag = new Tag();
                    new_tag.setTagName(tag);
                    tagMapper.insert(new_tag);
                    picTag.setTagId(new_tag.getId());
                }
                picTag.setPicId(pid);
                picTagMapper.insert(picTag);
            }
        }

    }

