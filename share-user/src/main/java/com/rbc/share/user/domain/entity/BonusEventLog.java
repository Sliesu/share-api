package com.rbc.share.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author DingYihang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BonusEventLog {
    private Long id;
    private Long userId;
    private Integer value;
    private String description;
    private String event;
    private Date createTime;
}