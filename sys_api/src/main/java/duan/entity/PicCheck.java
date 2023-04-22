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
 * @since 2023-04-20
 */
@Getter
@Setter
  @TableName("pic_check")
@ApiModel(value = "PicCheck对象", description = "")
public class PicCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

      @ApiModelProperty("图片id")
        private Integer pid;

      @ApiModelProperty("审核者id")
        private Integer uid;


}
