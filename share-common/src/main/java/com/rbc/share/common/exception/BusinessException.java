package com.rbc.share.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author DingYihang
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {
    private BusinessExceptionEnum e;
    public BusinessException(BusinessExceptionEnum e) {
        this.e = e;
    }
}
