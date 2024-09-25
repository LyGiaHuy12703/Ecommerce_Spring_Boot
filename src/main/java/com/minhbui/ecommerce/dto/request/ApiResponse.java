package com.minhbui.ecommerce.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
//null thì không kèm vào json
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{ // T Type truyen du lieu thuoc moi kieu
    int code = 1000; //api success
    String message;
    T data;
}
