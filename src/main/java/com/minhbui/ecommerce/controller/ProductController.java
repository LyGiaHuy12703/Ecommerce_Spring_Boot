package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.dto.request.*;
import com.minhbui.ecommerce.dto.response.ProductDetailResponse;
import com.minhbui.ecommerce.dto.response.ProductResponse;
import com.minhbui.ecommerce.dto.response.ProductSkusResponse;
import com.minhbui.ecommerce.model.Product;
import com.minhbui.ecommerce.model.ProductDetail;
import com.minhbui.ecommerce.model.ProductSkus;
import com.minhbui.ecommerce.repository.ProductRepository;
import com.minhbui.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ProductController {
    @Autowired
    private ProductService productService;

    //tạo sản phẩm
    @PostMapping("/products")
    ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductCreationRequest request) {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.craeteProductOfCategory(request));
        return apiResponse;
    }
    //lấy tất cả sản phẩm của shop sở hữu
    @GetMapping("/products")
    List<ProductResponse> getAllProductsOfShop() {
        return productService.getAllProductsOfShop();
    }
    //lấy tất cả sản phẩm của shop khi click vào shop
    @GetMapping("/productsShop/{shopId}")
     List<ProductResponse> getAllProductsOfShopForUser(@PathVariable("shopId") Long shopId) {
        return productService.getAllProductsOfShopForUser(shopId);
    }

    //lấy tất cả sản phẩm khi click vào category
    @GetMapping("/productsCategory/{categoryId}")
    List<ProductResponse> getAllProductsOfCategory(@PathVariable("categoryId") Long categoryId) {
        return productService.getAllProductsOfCategory(categoryId);
    }

    //lấy tưngf sản phẩm
    @GetMapping("/products/{id}")
    ApiResponse<Product> getProduct(@PathVariable("id") Long id) {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.getProductById(id));
        return apiResponse;
    }

    //cập nhật sản phẩm
    @PutMapping("/products/{id}")
    ApiResponse<Product> updateProduct(@PathVariable("id") Long id, @RequestBody @Valid ProductUpdateRequest request) {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.updateProduct( id, request));
        return apiResponse;
    }
    //xóa sản phẩm
    @DeleteMapping("/products/{id}")
    ApiResponse<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Product deleted")
                .build();
    }

    //product_skus
    @PostMapping("/productSkus")
    ApiResponse<ProductSkusResponse> addProductSkusToProduct(@RequestBody @Valid ProductSkusCreationRequest request) {
        ApiResponse<ProductSkusResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.addProductSkusToProduct(request));
        return apiResponse;
    }

    @GetMapping("/productSkus/{id}")
    ProductSkusResponse getProductSkus(@PathVariable("id") Long id) {
        return productService.getProductSkus(id);
    }

    @PutMapping("/productSkus/{id}")
    ProductSkusResponse updateProductSkus(@PathVariable("id") Long id, @RequestBody @Valid ProductSkusUpdateRequest request) {
        return productService.updateProductSkus(id, request);
    }

    @DeleteMapping("/productSkus/{id}")
    ApiResponse<String> deleteProductSkus(@PathVariable("id") Long id) {
        productService.deleteProductSkus(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Delete product skus success")
                .build();
    }

    //product detail
    @PostMapping("/productDetail")
    ApiResponse<ProductDetailResponse> addProductDetailToProduct(@RequestBody @Valid ProductDetailCreationRequest request) {
        ApiResponse<ProductDetailResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.addProductDetailToProduct(request));
        return apiResponse;
    }

    @GetMapping("productDetail/{id}")
    ProductDetailResponse getProductDetail(@PathVariable("id") Long id) {
        return productService.getProductDetailById(id);
    }
    @PutMapping("productDetail/{id}")
    ProductDetailResponse updateProductDetail(@PathVariable("id") Long id, @RequestBody @Valid ProductDetailUpdateRequest request) {
        return productService.updateProductDetail(id, request);
    }
    @DeleteMapping("/productDetail/{id}")
    ApiResponse<String> deleteProductDetail(@PathVariable("id") Long id) {
        productService.deleteProductDetail(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Delete product detail success")
                .build();
    }
}
