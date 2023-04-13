package duan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-04-10
 */
@Getter
@Setter
  @ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "uid", type = IdType.AUTO)
      private Integer uid;

    private String uuid;

      @ApiModelProperty("用户名")
      private String username;

      @ApiModelProperty("密码")
      private String pwd;

      @ApiModelProperty("区分角色权限ROOT，USER，VISITOR,STAFF")
      private String role;

      @ApiModelProperty("账号封禁状态ABLE或者UNABLE")
      private String status;


}
