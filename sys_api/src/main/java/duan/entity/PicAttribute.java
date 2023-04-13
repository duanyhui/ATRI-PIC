package duan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

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
 * @since 2023-04-10
 */
@Getter
@Setter
  @TableName("pic_attribute")
@ApiModel(value = "PicAttribute对象", description = "")
public class PicAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer pid;



      @ApiModelProperty("图片分辨率")
      private Integer width;

      @ApiModelProperty("图片分辨率")
      private Integer height;

      @ApiModelProperty("上传时间")
      private LocalDateTime updatetime;


}
