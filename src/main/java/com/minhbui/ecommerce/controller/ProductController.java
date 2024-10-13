package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.dto.product.ProductSkusAndAttributeRequest;
import com.minhbui.ecommerce.dto.product.SkusAndAttributeResponse;
import com.minhbui.ecommerce.dto.request.*;
import com.minhbui.ecommerce.dto.response.ProductAttributesResponse;
import com.minhbui.ecommerce.dto.response.ProductDetailResponse;
import com.minhbui.ecommerce.dto.response.ProductResponse;
import com.minhbui.ecommerce.dto.response.ProductSkusResponse;
import com.minhbui.ecommerce.model.Product;
import com.minhbui.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    //tạo productSKus và attribute
    @PostMapping("/shop/addSkusAndAttribute")
    ApiResponse<SkusAndAttributeResponse> createSkusAndAttribute(@RequestBody ProductSkusAndAttributeRequest request){
        ApiResponse<SkusAndAttributeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.createSkusAndAttribute(request));
        return apiResponse;
    }

    //tạo sản phẩm
    @PostMapping("/shop/products")
    ApiResponse<ProductResponse> createProduct(
            @ModelAttribute @Valid ProductCreationRequest request,
            @RequestParam List<MultipartFile> images
    ) throws IOException {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.craeteProductOfCategory(request, images));
        return apiResponse;
    }
    //lấy tất cả sản phẩm
    @GetMapping("/products")
    ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "12")int size) {
        Page<Product> productPage =  productService.getAllProducts(page, size);
        return ResponseEntity.ok(productPage);
    }

    //lấy tất cả sản phẩm của shop sở hữu
    @GetMapping("/shop/products")
    List<ProductResponse> getAllProductsOfShop(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "12")int size
    ) {
        return productService.getAllProductsOfShop(page, size);
    }
    //lấy tất cả sản phẩm của shop khi click vào shop có tăng giảm
    @GetMapping("/shop/{shopId}/productsShop/")
     List<Product> getAllProductsOfShopForUser(
            @PathVariable("shopId") Long shopId,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "12")int size,
            @RequestParam(defaultValue = "true") boolean ascending //giá trị tăng giảm
    ) {
        return productService.getAllProductsOfShopForUser(shopId, page, size, ascending);
    }

    //lấy tất cả sản phẩm khi click vào category có tăng giảm
    @GetMapping("/shop/productsCategory/{categoryId}")
    List<Product> getAllProductsOfCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "12")int size,
            @RequestParam(defaultValue = "true") boolean ascending //giá trị tăng giảm
    ) {
        return productService.getAllProductsOfCategory(categoryId, page, size, ascending);
    }

    //lấy tưngf sản phẩm
    @GetMapping("/shop/products/{id}")
    ApiResponse<Product> getProduct(@PathVariable("id") Long id) {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.getProductById(id));
        return apiResponse;
    }

    //cập nhật sản phẩm
    @PutMapping("/shop/products/{id}")
    ApiResponse<Product> updateProduct(
            @PathVariable("id") Long id,
            @ModelAttribute @Valid ProductUpdateRequest request,
            @RequestParam List<MultipartFile> images) throws IOException {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.updateProduct( id, request, images));
        return apiResponse;
    }
    //xóa sản phẩm
    @DeleteMapping("/shop/products/{id}")
    ApiResponse<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Product deleted")
                .build();
    }

    //product_skus
    @PostMapping("/shop/productSkus")
    ApiResponse<ProductSkusResponse> addProductSkusToProduct(@RequestBody @Valid ProductSkusCreationRequest request) {
        ApiResponse<ProductSkusResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.addProductSkusToProduct(request));
        return apiResponse;
    }

    @GetMapping("/shop/productSkus/{id}")
    ProductSkusResponse getProductSkus(@PathVariable("id") Long id) {
        return productService.getProductSkus(id);
    }

    @PutMapping("/shop/productSkus/{id}")
    ProductSkusResponse updateProductSkus(@PathVariable("id") Long id, @RequestBody @Valid ProductSkusUpdateRequest request) {
        return productService.updateProductSkus(id, request);
    }

    @DeleteMapping("/shop/productSkus/{id}")
    ApiResponse<String> deleteProductSkus(@PathVariable("id") Long id) {
        productService.deleteProductSkus(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Delete product skus success")
                .build();
    }

    //product detail
    @PostMapping("/shop/productDetail")
    ApiResponse<ProductDetailResponse> addProductDetailToProduct(@RequestBody @Valid ProductDetailCreationRequest request) {
        ApiResponse<ProductDetailResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.addProductDetailToProduct(request));
        return apiResponse;
    }

    @GetMapping("/shop/productDetail/{id}")
    ProductDetailResponse getProductDetail(@PathVariable("id") Long id) {
        return productService.getProductDetailById(id);
    }
    @PutMapping("/shop/productDetail/{id}")
    ProductDetailResponse updateProductDetail(@PathVariable("id") Long id, @RequestBody @Valid ProductDetailUpdateRequest request) {
        return productService.updateProductDetail(id, request);
    }
    @DeleteMapping("/shop/productDetail/{id}")
    ApiResponse<String> deleteProductDetail(@PathVariable("id") Long id) {
        productService.deleteProductDetail(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Delete product detail success")
                .build();
    }

    //product attributes
    @PostMapping("/shop/productAttribute")
    ApiResponse<ProductAttributesResponse> addProductAttributesToProduct(@RequestBody @Valid ProductAttributesCreateRequest request){
        ApiResponse<ProductAttributesResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.addProductAttributes(request));
        return apiResponse;
    }

    @GetMapping("/shop/productAttribute/{id}")
    ProductAttributesResponse getProductAttribute(@PathVariable("id") Long id) {
        return productService.getProductAttributeById(id);
    }

    @PutMapping("/shop/productAttribute/{id}")
    ProductAttributesResponse updateProductAttribute(@PathVariable("id") Long id, @RequestBody @Valid ProductAttributesUpdateRequest request) {
        return productService.updateProductAttribute(id, request);
    }

    @DeleteMapping("/shop/productAttribute/{id}")
    ApiResponse<String> deleteProductAttribute(@PathVariable("id") Long id) {
        productService.deleteProductAttribute(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Delete product attribute success")
                .build();
    }

    // Endpoint tìm kiếm sản phẩm theo tên
    @GetMapping("products/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam("name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> result = productService.searchProductsByName(name, pageable);
        return ResponseEntity.ok(result);
    }

    // Endpoint tìm kiếm theo tên và sắp xếp theo giá
    @GetMapping("products/search/sorted")
    public ResponseEntity<Page<Product>> searchProductsSortedByPrice(
            @RequestParam("name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> result = productService.searchProductsByNameAndSortByPrice(name, pageable);
        return ResponseEntity.ok(result);
    }

    // Endpoint tìm kiếm theo shop và trong khoảng giá
    @GetMapping("products/search/price")
    public ResponseEntity<Page<Product>> searchProductsByShopAndPriceRange(
            @RequestParam("shopId") Long shopId,
            @RequestParam("minPrice") Long minPrice,
            @RequestParam("maxPrice") Long maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> result = productService.searchProductsByShopAndPriceRange(shopId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(result);
    }
}
