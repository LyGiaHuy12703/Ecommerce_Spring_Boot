package com.minhbui.ecommerce.enums;

public enum RoleEnum {
    ROLE_USER(0),
    ROLE_SHOP(1),
    ROLE_ADMIN(2);
    private final int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoleEnum fromValue(int value) {
        for (RoleEnum status : RoleEnum.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
