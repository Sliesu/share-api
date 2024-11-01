package com.rbc.share.content.domain.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author DingYihang
 */
@Data
public class Notice {

    private Long id;
    private String content;
    private Boolean showFlag;

    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd:mm:ss")
    private Date createTime;
}
