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
    ORDER_NOT_FOUND(1020,"order not found"),
    ORDER_STATUS_INVALID(1019, "order status invalid"),
    CART_EMPTY(1018,"CART_EMPTY"),
    PRODUCT_ATTRIBUTE_NOT_FOUND(1017,"product attribute not found"),
    ITEM_NOT_FOUND_IN_CART(1016, "Không tìm thấy sản phẩm trong giỏ hàng"),
    INSUFFICIENT_QUANTITY(1015, "Không đủ số lượng"),
    CART_NOT_FOUND(1014, "Cart not found"),
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
