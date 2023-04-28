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
 * @since 2023-04-26
 */
@Getter
@Setter
  @TableName("pic_num")
@ApiModel(value = "PicNum对象", description = "")
public class PicNum implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer pid;

    private Integer likenum;

    private Integer loadnum;

    private Integer seenum;


}
