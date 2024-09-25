package com.minhbui.ecommerce.service;

import com.minhbui.ecommerce.dto.request.*;
import com.minhbui.ecommerce.dto.response.ProductDetailResponse;
import com.minhbui.ecommerce.dto.response.ProductResponse;
import com.minhbui.ecommerce.dto.response.ProductSkusResponse;
import com.minhbui.ecommerce.exception.AppCatchException;
import com.minhbui.ecommerce.exception.ErrorCode;
import com.minhbui.ecommerce.mapper.ProductDetailMapper;
import com.minhbui.ecommerce.mapper.ProductMapper;
import com.minhbui.ecommerce.mapper.ProductskusMapper;
import com.minhbui.ecommerce.model.*;
import com.minhbui.ecommerce.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    ProductSkusRepository productSkusRepository;
    ProductskusMapper productSkusMapper;
    ProductDetailRepository productDetailRepository;
    ProductDetailMapper productDetailMapper;
    ShopRepository shopRepository;
    EventRepository eventRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    //product

    public ProductResponse craeteProductOfCategory(ProductCreationRequest request) {

        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.SHOP_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->new AppCatchException(ErrorCode.CATEGORY_NOT_FOUND));

        if(!category.getShop().equals(shop)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        if(category.isDisabled()){
            throw  new AppCatchException(ErrorCode.CATEGORY_DISABLE);
        }

        Product product = productMapper.toProduct(request);

        product.setShop(shop);
        product.setCategory(category);

        Date currentDate = new Date();

        product.setCreatedAt(currentDate);

        product.setDisabled(false);

        productRepository.save(product);
        ProductResponse productResponse = productMapper.toProductResponse(product);
        productResponse.setCategory(category);
        return productResponse;
    }

    //lấy tất cả sản phẩm của shop sở hữu
    public List<ProductResponse> getAllProductsOfShop() {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        return productMapper.toProductResponseList(productRepository.findProductsByShopWithDisabledCategory(shop));
    }

    //lấy tất cả sản phẩm của shop khi click vào shop
    public List<ProductResponse> getAllProductsOfShopForUser(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() ->new AppCatchException(ErrorCode.SHOP_NOT_FOUND));

        return productMapper.toProductResponseList(productRepository.findProductsByShopWithDisabledCategory(shop));
    }

    //lấy tất cả sản phẩm khi click vào category
    public List<ProductResponse> getAllProductsOfCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->new AppCatchException(ErrorCode.CATEGORY_NOT_FOUND));

        return productMapper.toProductResponseList(productRepository.findProductsByCategory(category));
    }

    //lấy tưngf sản phẩm
    public Product getProductById(Long id) {

        Product product = productRepository.findById(id).orElse(null);

        if(product.isDisabled()){
            throw new AppCatchException(ErrorCode.PRODUCT_DISABLE);
        }

        return product;
    }

    public Product updateProduct(Long id, ProductUpdateRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        productMapper.updateProduct(request, product);
//        Event event = eventRepository.findById(request.getEventId()).orElse(null);
        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);

        if(!category.getShop().equals(shop)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        product.setShop(shop);
        product.setCategory(category);
//        product.setEvent(event);
//        product.onCreate();
        return productRepository.save(product);
    }
    public void deleteProduct(Long id) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        Product product = productRepository.findById(id)
                        .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }
        product.setDisabled(true);
        productRepository.save(product);
    }

    //product skus
    public ProductSkusResponse addProductSkusToProduct(ProductSkusCreationRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        ProductSkus productSkus = productSkusMapper.toProductSkus(request);

        productSkus.setProduct(product);

        Date currentDate = new Date();
        productSkus.setCreatedAt(currentDate);

        productSkusRepository.save(productSkus);
        ProductSkusResponse productSkusResponse = productSkusMapper.toProductSkusResponse(productSkus);
        productSkusResponse.setProduct(product);
        return productSkusResponse;
    }
//    public List<ProductSkusResponse> getProductSkus(){
//        return productSkusMapper.toProductSkusResponseList(productSkusRepository.findAll());
//    }
    public ProductSkusResponse getProductSkus(Long id) {

        ProductSkus productSkus = productSkusRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_SKUS_NOT_FOUND));

        return productSkusMapper.toProductSkusResponse(productSkus);
    }
    public ProductSkusResponse updateProductSkus(Long id, ProductSkusUpdateRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        shopRepository.findByOwner(owner).orElseThrow(()->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        ProductSkus productSkus = productSkusRepository.findById(id)
                        .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_SKUS_NOT_FOUND));
        productSkusMapper.updateProductSkus(productSkus, request);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        Date currentDate = new Date();
        product.setCreatedAt(currentDate);
        productSkus.setProduct(product);
        return productSkusMapper.toProductSkusResponse(productSkusRepository.save(productSkus));

    }
    public void deleteProductSkus(Long id) {
        ProductSkus productSkus = productSkusRepository.findById(id)
                        .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_SKUS_NOT_FOUND));
        productSkusRepository.deleteById(id);
    }

    //product detail
    public ProductDetailResponse addProductDetailToProduct(ProductDetailCreationRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        shopRepository.findByOwner(owner)
                .orElseThrow(()->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        ProductDetail productDetail = productDetailMapper.toProductDetail(request);

        productDetail.setProduct(product);
        ProductDetailResponse productDetailResponse = productDetailMapper.toProductDetailResponse(productDetailRepository.save(productDetail));
        productDetailResponse.setProduct(product);
        return productDetailResponse;
    }

    public ProductDetailResponse getProductDetailById(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        ProductDetailResponse productDetailResponse = productDetailMapper.toProductDetailResponse(productDetail);
        productDetailResponse.setProduct(productDetail.getProduct());
        return productDetailResponse;
    }
    public ProductDetailResponse updateProductDetail(Long id, ProductDetailUpdateRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        shopRepository.findByOwner(owner)
                .orElseThrow(()->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        ProductDetail productDetail = productDetailRepository.findById(id)
                    .orElseThrow(()-> new AppCatchException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        productDetailMapper.updateProductDetail(productDetail, request);

        productDetail.setProduct(product);

        return productDetailMapper.toProductDetailResponse(productDetailRepository.save(productDetail));
    }
    public void deleteProductDetail(Long id) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        shopRepository.findByOwner(owner)
                .orElseThrow(()->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!productDetail.getProduct().getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        productDetailRepository.deleteById(id);
    }

}