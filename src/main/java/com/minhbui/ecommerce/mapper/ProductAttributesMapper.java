package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.ProductAttributesCreateRequest;
import com.minhbui.ecommerce.dto.request.ProductAttributesUpdateRequest;
import com.minhbui.ecommerce.dto.response.ProductAttributesResponse;
import com.minhbui.ecommerce.model.ProductAttributes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductAttributesMapper {
    ProductAttributes toProductAttributes(ProductAttributesCreateRequest productAttributesCreateRequest);
    ProductAttributesResponse toProductAttributesResponse(ProductAttributes productAttributes);
}
