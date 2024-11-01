package com.rbc.share.common.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DingYihang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResp<T> {
    private Boolean success = true;
    private String message;
    private T data;
}