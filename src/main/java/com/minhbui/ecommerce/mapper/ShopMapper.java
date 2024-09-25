package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.response.ShopResponse;
import com.minhbui.ecommerce.model.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    @Mapping(target = "token",ignore = true)
    ShopResponse toShopResponse(Shop shop);
}
