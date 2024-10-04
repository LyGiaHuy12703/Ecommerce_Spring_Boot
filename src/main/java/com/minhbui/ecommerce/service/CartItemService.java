package com.minhbui.ecommerce.service;

import com.minhbui.ecommerce.dto.request.ApiResponse;
import com.minhbui.ecommerce.dto.request.CartItemRequest;
import com.minhbui.ecommerce.dto.response.CartItemResponse;
import com.minhbui.ecommerce.exception.AppCatchException;
import com.minhbui.ecommerce.exception.ErrorCode;
import com.minhbui.ecommerce.mapper.CartItemMapper;
import com.minhbui.ecommerce.model.*;
import com.minhbui.ecommerce.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemService {
    CartItemRepository cartItemRepository;

    CartItemMapper cartItemMapper;
    UserRepository userRepository;
    CartRepository cartRepository;
    ProductSkusRepository productSkusRepository;
    ProductRepository productRepository;

    @PreAuthorize("hasRole('USER')")
    public CartItemResponse addItemToCart(CartItemRequest request ) {
        //laays id người thêm
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email).orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findById(owner.getId()).orElseThrow(() -> new AppCatchException(ErrorCode.CART_NOT_FOUND));

        //lấy productSkus
        ProductSkus productSkus = productSkusRepository.findById(request.getProductSku())
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_SKUS_NOT_FOUND));

        //tìm sản phẩm trong cơ sở dữ liệu
        Product product = productRepository.findById(productSkus.getId())
                .orElseThrow(() -> new AppCatchException(ErrorCode.PRODUCT_NOT_FOUND));

        //kiểm tra số lượng < số lượng người mua muốn
        if(productSkus.getStockQuantity() < request.getQuantity()){
            throw  new AppCatchException(ErrorCode.INSUFFICIENT_QUANTITY);
        }

        //Kiểm tra sản phẩm đã có trong giỏ hàng chưa nếu chưa thì tạo mới
        CartItem cartItem = cartItemRepository.findByCartAndProductSku(cart, productSkus)
                .orElse(new CartItem(cart, productSkus, 0,0));

        //cập nhật số lượng và giá tiền sản phẩm trong giỏ hàng
        cartItem.setQuantity(request.getQuantity());
        cartItem.setTotalPrice(request.getTotalPrice()*request.getQuantity());
        cartItemRepository.save(cartItem);

        //cập nhật tổng giá trị giỏ hàng
        cart.setTotal(cart.getTotal() + request.getQuantity());
        cartRepository.save(cart);

        return cartItemMapper.toCartItemResponse(cartItem);
    }
    //lấy tất cả item trong cart
    @PreAuthorize("hasRole('USER')")
    public List<CartItemResponse> getCartItems() {
        //laays id người dungf
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email).orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findById(owner.getId()).orElseThrow(() -> new AppCatchException(ErrorCode.CART_NOT_FOUND));

        return cartItemMapper.toResponseListCartItem(cartItemRepository.findByCart(cart));
    }
    //cập nhật số lượng item
    @PreAuthorize("hasRole('USER')")
    public CartItemResponse updateQuantityItem(CartItemRequest request ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email).orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findById(owner.getId()).orElseThrow(() -> new AppCatchException(ErrorCode.CART_NOT_FOUND));

        //lấy danh sách cartitem set -> giống lấy 1
        Set<CartItem> cartItems = cart.getCartItems();

        //lấy cartItem cần update
        CartItem cartItemUpdate = cartItems.stream().filter(cartItem ->
                cartItem.equals(request.getCart().getCartItems())).findFirst().orElseThrow(() -> new AppCatchException(ErrorCode.ITEM_NOT_FOUND_IN_CART));

        //lấy số tiền của một món cần update
        var priceOneItem = cartItemUpdate.getTotalPrice()/cartItemUpdate.getQuantity();
        //giá mới khi update
        var newPriceItem = priceOneItem*request.getQuantity();
        //giá gốc của item
        var originalPrice = cartItemUpdate.getTotalPrice();
        //lấy số lượng gốc của item
        var originQuantity = cartItemUpdate.getQuantity();

        cartItemUpdate.setTotalPrice(newPriceItem);
        cartItemUpdate.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItemUpdate);

        cart.getTotalItemsInCart();

        return cartItemMapper.toCartItemResponse(cartItemUpdate);
    }

    //xóa sản phẩm
    @PreAuthorize("hasRole('USER')")
    public void clearItem(Long id){
        //laays id người dungf
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email).orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findById(owner.getId()).orElseThrow(() -> new AppCatchException(ErrorCode.CART_NOT_FOUND));
        //tìm cart item cần xóa
        CartItem cartItem = cart.getCartItems().stream().filter(cartItem1 ->
                cartItem1.getId().equals(id)).findFirst().orElseThrow((() -> new AppCatchException(ErrorCode.ITEM_NOT_FOUND_IN_CART)));

        cart.removeCartItem(cartItem);
        cart.setTotal(cart.getTotal() - cartItem.getQuantity());
        cartRepository.save(cart);
    }

    //xóa toàn bộ item trong cart
    @PreAuthorize("hasRole('USER')")
    public void clearAllItems() {
        //laays id người dungf
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email).orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findById(owner.getId()).orElseThrow(() -> new AppCatchException(ErrorCode.CART_NOT_FOUND));

        cart.removeAllCartItems();
        cart.setTotal(0L);
        cartRepository.save(cart);
    }
}
