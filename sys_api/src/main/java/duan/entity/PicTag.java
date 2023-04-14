package duan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author duanyhui
 * @since 2023-04-14
 */
@Getter
@Setter
  @TableName("pic_tag")
@ApiModel(value = "PicTag对象", description = "")
public class PicTag implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer picId;

    private Integer tagId;


}
