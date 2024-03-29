package duan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-20
 */
@Getter
@Setter
  @TableName("pic_update")
@ApiModel(value = "PicUpdate对象", description = "")
public class PicUpdate implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String uuid;
    private Integer pid;

    @ApiModelProperty("图片上传者的邮箱")
    private String mail;


}
