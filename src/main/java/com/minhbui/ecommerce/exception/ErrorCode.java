package com.minhbui.ecommerce.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    ADDRESS_NOT_FOUND(1013,"Address Not Found"),
    PRODUCT_DISABLE(1012, "Product disabled"),
    CATEGORY_DISABLE(1011,"Category disabled"),
    USER_NOT_FOUND(1010,"user not found"),
    YOU_ARE_NOT_OWNER(1009,"You are not owner."),
    EMAIL_NOT_FOUND(1008, "Email Not Found"),
    UNCATEGORZED(9999, "Uncategorized error"),
    PRODUCT_DETAIL_NOT_FOUND(1007, "Product Detail Not Found"),
    PRODUCT_SKUS_NOT_FOUND(1006, "Product Skus Not Found"),
    SHOP_NOT_FOUND(1005, "Shop not found"),
    INVALID_KEY(1004,"Invalid key"),
    PRODUCT_NOT_FOUND(1003, "Product Not Found"),
    CATEGORY_NOT_FOUND(1001, "Category not found"),
    EVENT_NOT_FOUND(1002, "Event not found");


    int code;
    String message;
}
