package com.minhbui.ecommerce.exception;

import com.minhbui.ecommerce.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

//thông báo xử lý lỗi by Gia Huy
@ControllerAdvice
public class GlobalExceptionHandler {
    //catch all exception
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(DefaultHandlerExceptionResolver e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORZED.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORZED.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppCatchException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppCatchException e) {
        ErrorCode errorCode = e.getErrorCode();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode .getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e) {
        ErrorCode errorCode = ErrorCode.UNCATEGORZED;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode .getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
