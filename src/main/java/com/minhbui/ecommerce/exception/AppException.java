package com.minhbui.ecommerce.exception;

import com.minhbui.ecommerce.enums.ErrorEnum;
import lombok.Builder;

@Builder
public class AppException extends RuntimeException {

    ErrorEnum error;

    public AppException(ErrorEnum error) {
        this.error = error;
    }

    public ErrorEnum getError() {
        return error;
    }

    public void setError(ErrorEnum error) {
        this.error = error;
    }


}
