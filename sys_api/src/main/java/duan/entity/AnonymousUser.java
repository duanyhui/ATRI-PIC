package duan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-04-20
 */
@Getter
@Setter
  @TableName("anonymous_user")
@ApiModel(value = "AnonymousUser对象", description = "")
public class AnonymousUser implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("匿名用户类")
        @TableId(value = "uid", type = IdType.AUTO)
      private Integer uid;

    private String uuid;

    private String name;

    private LocalDateTime createTime;

    private String ip;
    private String address;
    private String browser;
    private String os;


}
