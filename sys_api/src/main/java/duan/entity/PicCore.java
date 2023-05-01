package duan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@Data
  @TableName("pic_core")
@ApiModel(value = "PicCore对象", description = "")
public class PicCore implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "pid", type = IdType.AUTO)
      private Integer pid;

      @ApiModelProperty("文件名")
      private String filename;

      private String info;

      @ApiModelProperty("cdn图片链接")
      private String cdnurl;
      @ApiModelProperty("本地url")
      private String localurl;

      @ApiModelProperty("缩略图url")
      private String miniurl;


      @ApiModelProperty("审核状态（默认不公开）ABLE/UNABLE/FORBID")
      private String status;

      private String author;

      private String source;


}
