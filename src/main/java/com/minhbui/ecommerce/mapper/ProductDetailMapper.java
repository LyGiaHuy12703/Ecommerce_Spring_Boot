package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.ProductDetailCreationRequest;
import com.minhbui.ecommerce.dto.request.ProductDetailUpdateRequest;
import com.minhbui.ecommerce.dto.response.ProductDetailResponse;
import com.minhbui.ecommerce.model.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {
    ProductDetail toProductDetail(ProductDetailCreationRequest request);
    void updateProductDetail(@MappingTarget ProductDetail productDetail, ProductDetailUpdateRequest request);
    ProductDetailResponse toProductDetailResponse(ProductDetail productDetail);
    List<ProductDetailResponse> toProductDetailResponseList(List<ProductDetail> productDetails);
}
