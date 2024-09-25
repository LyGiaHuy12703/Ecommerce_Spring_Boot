package com.minhbui.ecommerce.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppCatchException extends RuntimeException {
    public AppCatchException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.errorCode = errorCode;
    }

    ErrorCode errorCode;
}
