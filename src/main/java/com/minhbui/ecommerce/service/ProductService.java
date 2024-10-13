package com.minhbui.ecommerce.service;

import com.cloudinary.utils.ObjectUtils;
import com.minhbui.ecommerce.config.CloudinaryConfig;
import com.minhbui.ecommerce.dto.product.ProductSkusAndAttributeRequest;
import com.minhbui.ecommerce.dto.product.SkusAndAttributeResponse;
import com.minhbui.ecommerce.dto.request.*;
import com.minhbui.ecommerce.dto.response.ProductAttributesResponse;
import com.minhbui.ecommerce.dto.response.ProductDetailResponse;
import com.minhbui.ecommerce.dto.response.ProductResponse;
import com.minhbui.ecommerce.dto.response.ProductSkusResponse;
import com.minhbui.ecommerce.exception.AppCatchException;
import com.minhbui.ecommerce.exception.ErrorCode;
import com.minhbui.ecommerce.mapper.ProductAttributesMapper;
import com.minhbui.ecommerce.mapper.ProductDetailMapper;
import com.minhbui.ecommerce.mapper.ProductMapper;
import com.minhbui.ecommerce.mapper.ProductskusMapper;
import com.minhbui.ecommerce.model.*;
import com.minhbui.ecommerce.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    ProductAttributesMapper productAttributesMapper;
    ProductAttributesRepository productAttributesRepository;
    UserService userService;
    CloudinaryConfig cloudinary;

    @PreAuthorize("hasRole('SHOP')")
    public SkusAndAttributeResponse createSkusAndAttribute(ProductSkusAndAttributeRequest request){
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.SHOP_NOT_FOUND));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductSkus productSkus = ProductSkus.builder()
                .createdAt(new Date())
                .product(product)
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();
        productSkusRepository.save(productSkus);


        ProductAttributes productAttributes = new ProductAttributes();

        if(shop.getProducts().contains(product)){
            Boolean isType = productAttributesRepository.existsByType(request.getType());
            Boolean isValue = productAttributesRepository.existsByValue(request.getValue());
            log.info("code ddeens day");
            if(isType && isValue){
                log.info("code ddeens day2");
                Set<ProductAttributes> productAttribute = productAttributesRepository.findProductAttributesByTypeAndValue(request.getType(), request.getValue());
                productSkus.setProductAttributes(productAttribute);
                productSkusRepository.save(productSkus);

            }else{
                log.info("code ddeens day3");
                productAttributes.setCreatedAt(new Date());
                productAttributes.setType(request.getType());
                productAttributes.setValue(request.getValue());
                log.error("error: ", productAttributes);
                productAttributes.setProductSku(productSkus);

                try {
                    productAttributesRepository.save(productAttributes);
                } catch (Exception e) {
                    log.error("Error saving productAttributes: ", e);
                    throw new AppCatchException(ErrorCode.ADDRESS_NOT_FOUND );
                }
            }
        }else{
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }
        return SkusAndAttributeResponse.builder()
              .product(product)
                .build();
    }

    //product
    @PreAuthorize("hasRole('SHOP')")
    public ProductResponse craeteProductOfCategory(
            ProductCreationRequest request,
            List<MultipartFile> files
    ) throws IOException {

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

        Set<String> images = uploadMultiImg(files, String.valueOf(owner.getId()));

        Product product = productMapper.toProduct(request);
        product.setImages(images);

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

    private Set<String> uploadMultiImg(List<MultipartFile> files, String unique) throws IOException {
        Set<String> urlList = new HashSet<>();
        for (MultipartFile file : files) {
            var result = cloudinary.cloudinary().uploader()
                    .upload(file.getBytes(), ObjectUtils.asMap(
                            "folder", "ecommerce/shop_owner_"+unique+"/products"
                    ));
            urlList.add((String) result.get("secure_url"));;
        }
        return urlList;
    }

    //lấy tất cả sản phẩm
    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return productRepository.findALlByDisabledFalse(pageable);
    }

    //lấy tất cả sản phẩm của shop sở hữu method for shop
    @PreAuthorize("hasRole('SHOP')")
    public List<ProductResponse> getAllProductsOfShop(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        return productMapper.toProductResponseList(productRepository.findProductsByShopWithDisabledCategory(shop, pageable));
    }

    //tìm kieems
    //tìm kiếm theo tên
    public Page<Product> searchProductsByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }
    //tìm kiếm theo tên và sắp xếp giá
    public Page<Product> searchProductsByNameAndSortByPrice(String name, Pageable pageable) {
        return productRepository.findByNameOrderByPriceAsc(name, pageable);
    }
    //tìm kiếm theo tên và khoản giá trong shop
    public Page<Product> searchProductsByShopAndPriceRange(Long shopId, Long minPrice, Long maxPrice, Pageable pageable) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() ->new AppCatchException(ErrorCode.SHOP_NOT_FOUND));
        return productRepository.findByShopAndPriceRange(shop, minPrice, maxPrice, pageable);
    }

    //lấy tất cả sản phẩm của shop khi click vào shop method for user
    public List<Product> getAllProductsOfShopForUser(Long shopId, int page, int size, boolean asc) {
        Pageable pageable = PageRequest.of(page,size);
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() ->new AppCatchException(ErrorCode.SHOP_NOT_FOUND));

        if(asc){
            return productRepository.findByShopOrderByMinPriceAsc(shop, pageable);
        }else {
            return productRepository.findByShopOrderByMinPriceDesc(shop, pageable);
        }
    }

    //lấy tất cả sản phẩm khi click vào category
    public List<Product> getAllProductsOfCategory(Long categoryId, int page, int size, boolean asc) {
        Pageable pageable = PageRequest.of(page,size);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->new AppCatchException(ErrorCode.CATEGORY_NOT_FOUND));

        if(asc){
            return productRepository.findByShopOrderByMinPriceAsc(category.getShop(), pageable);
        }else {
            return productRepository.findByShopOrderByMinPriceDesc(category.getShop(), pageable);
        }
    }

    //lấy tưngf sản phẩm
    public Product getProductById(Long id) {

        Product product = productRepository.findById(id).orElse(null);

        if(product.isDisabled()){
            throw new AppCatchException(ErrorCode.PRODUCT_DISABLE);
        }

        return product;
    }

    @PreAuthorize("hasRole('SHOP')")
    public Product updateProduct(Long id, ProductUpdateRequest request, List<MultipartFile> images) throws IOException {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() ->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        productMapper.updateProduct(request, product);
        Set<String> imagesUrl = uploadMultiImg(images, String.valueOf(owner.getId()));
        product.setImages(imagesUrl);
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
    @PreAuthorize("hasRole('SHOP')")
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
    @PreAuthorize("hasRole('SHOP')")
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
    @PreAuthorize("hasRole('SHOP')")
    public ProductSkusResponse getProductSkus(Long id) {

        ProductSkus productSkus = productSkusRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_SKUS_NOT_FOUND));

        return productSkusMapper.toProductSkusResponse(productSkus);
    }
    @PreAuthorize("hasRole('SHOP')")
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
    @PreAuthorize("hasRole('SHOP')")
    public void deleteProductSkus(Long id) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        shopRepository.findByOwner(owner)
                .orElseThrow(()->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!productDetail.getProduct().getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }
        productSkusRepository.deleteById(id);
    }

    //product detail
    @PreAuthorize("hasRole('SHOP')")
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

    @PreAuthorize("hasRole('SHOP')")
    public ProductDetailResponse getProductDetailById(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        ProductDetailResponse productDetailResponse = productDetailMapper.toProductDetailResponse(productDetail);
        productDetailResponse.setProduct(productDetail.getProduct());
        return productDetailResponse;
    }
    @PreAuthorize("hasRole('SHOP')")
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
    @PreAuthorize("hasRole('SHOP')")
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

    //proudct Attribute
    @PreAuthorize("hasRole('SHOP')")
    public ProductAttributesResponse addProductAttributes(ProductAttributesCreateRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

//        shopRepository.findByOwner(owner)
//                .orElseThrow(()->new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER));

        ProductSkus productSkus = productSkusRepository.findById(request.getProductSku().getId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_SKUS_NOT_FOUND));

        Product product = productRepository.findById(productSkus.getProduct().getId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        ProductAttributes productAttributes = productAttributesMapper.toProductAttributes(request);

        Date currentDate = new Date();
        productAttributes.setCreatedAt(currentDate);

        productAttributes.setProductSku(productSkus);
        productAttributesRepository.save(productAttributes);

        return productAttributesMapper.toProductAttributesResponse(productAttributes);
    }
    @PreAuthorize("hasRole('SHOP')")
    public ProductAttributesResponse getProductAttributeById(Long id) {
        ProductAttributes productAttributes = productAttributesRepository.findById(id)
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND));

        ProductAttributesResponse productAttributesResponse = productAttributesMapper.toProductAttributesResponse(productAttributes);
        productAttributesResponse.setProductSku(productAttributes.getProductSku());
        return productAttributesResponse;
    }
    @PreAuthorize("hasRole('SHOP')")
    public ProductAttributesResponse updateProductAttribute(Long id, ProductAttributesUpdateRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        ProductAttributes productAttributes = productAttributesRepository.findById(id)
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND));

        ProductSkus productSkus = productSkusRepository.findById(productAttributes.getProductSku().getId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_SKUS_NOT_FOUND));

        Product product = productRepository.findById(productSkus.getProduct().getId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        productAttributes.setValue(request.getValue());
        productAttributes.setType(request.getType());

        Date currentDate = new Date();
        productAttributes.setCreatedAt(currentDate);

        productAttributes.setProductSku(productSkus);
        productAttributesRepository.save(productAttributes);

        ProductAttributesResponse productAttributesResponse = productAttributesMapper.toProductAttributesResponse(productAttributes);
        productAttributesResponse.setProductSku(productAttributes.getProductSku());
        return productAttributesResponse;
    }

    @PreAuthorize("hasRole('SHOP')")
    public void deleteProductAttribute(Long id) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        ProductAttributes productAttributes = productAttributesRepository.findById(id)
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND));

        ProductSkus productSkus = productSkusRepository.findById(productAttributes.getProductSku().getId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_SKUS_NOT_FOUND));

        Product product = productRepository.findById(productSkus.getProduct().getId())
                .orElseThrow(()->new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getShop().getOwner().equals(owner)){
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        productAttributesRepository.deleteById(productAttributes.getId());
    }
}