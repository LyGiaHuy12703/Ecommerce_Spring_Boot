package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddressUpdateRequest {
    String city;
    String district;
    String ward;
    String street;
    String postalCode;
    String phoneNumber;
    String houseNumber;
}
