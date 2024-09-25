package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.dto.request.AddressCreationRequest;
import com.minhbui.ecommerce.dto.request.AddressUpdateRequest;
import com.minhbui.ecommerce.dto.request.ApiResponse;
import com.minhbui.ecommerce.dto.response.AddressResponse;
import com.minhbui.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressService addressService;

    @PostMapping
    ApiResponse<AddressResponse> createAddress(@RequestBody AddressCreationRequest request) {
        ApiResponse<AddressResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(addressService.createAddress(request));
        return apiResponse;
    }
    //lấy tất cả địa chỉ
    @GetMapping
    List<AddressResponse> getAllAddresses() {
        return addressService.getAll();
    }

    //lấy địa chỉ bằng id
    @GetMapping("{id}")
    AddressResponse getAddressById(@PathVariable("id") Long id) {
        return addressService.getAddressById(id);
    }

    @PutMapping("{id}")
    ApiResponse<AddressResponse> updateAddress(@PathVariable("id") Long id, @RequestBody AddressUpdateRequest request) {
        ApiResponse<AddressResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(addressService.updateAddress(id, request));
        return apiResponse;
    }
    @DeleteMapping("{id}")
    ApiResponse<String> deleteAddress(@PathVariable("id") Long id){
        addressService.deleteAddress(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData("Deleted Address successfully");
        return apiResponse;
    }
}
