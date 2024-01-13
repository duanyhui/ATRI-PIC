package duan.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Log_VO {
    private Integer id;

    private String uuid;

    private String log;

    private String url;

    private LocalDateTime time;

    private String type;
    private String ip;
    private String address;
    private String browser;
    private String os;
    private String name;
}
