package duan.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PicDetail_VO {
    private Integer pid;
//    @ApiModelProperty("文件名")
//    private String filename;

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
    private List<String> tags;
    private Integer likenum;

    private Integer loadnum;

    private Integer seenum;

}
