package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.ProductSkusCreationRequest;
import com.minhbui.ecommerce.dto.request.ProductSkusUpdateRequest;
import com.minhbui.ecommerce.dto.response.ProductSkusResponse;
import com.minhbui.ecommerce.model.ProductSkus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductskusMapper {
    ProductSkus toProductSkus(ProductSkusCreationRequest productSkus);
    void updateProductSkus(@MappingTarget ProductSkus productSkus, ProductSkusUpdateRequest request);
    ProductSkusResponse toProductSkusResponse(ProductSkus productSkus);
    List<ProductSkusResponse> toProductSkusResponseList(List<ProductSkus> productSkus);
}
