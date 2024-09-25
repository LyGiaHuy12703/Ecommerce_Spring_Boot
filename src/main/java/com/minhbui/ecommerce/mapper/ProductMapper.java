package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.ProductCreationRequest;
import com.minhbui.ecommerce.dto.request.ProductUpdateRequest;
import com.minhbui.ecommerce.dto.response.ProductResponse;
import com.minhbui.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductCreationRequest request);
    void updateProduct(ProductUpdateRequest request, @MappingTarget Product product);
    ProductResponse toProductResponse(Product product);
    List<ProductResponse> toProductResponseList(List<Product> products);
}
