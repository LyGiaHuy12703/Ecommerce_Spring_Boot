package com.minhbui.ecommerce.service;

import com.minhbui.ecommerce.dto.request.AddressCreationRequest;
import com.minhbui.ecommerce.dto.request.AddressUpdateRequest;
import com.minhbui.ecommerce.dto.response.AddressResponse;
import com.minhbui.ecommerce.exception.AppCatchException;
import com.minhbui.ecommerce.exception.ErrorCode;
import com.minhbui.ecommerce.mapper.AddressMapper;
import com.minhbui.ecommerce.model.Address;
import com.minhbui.ecommerce.model.User;
import com.minhbui.ecommerce.repository.AddressRepository;
import com.minhbui.ecommerce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AddressService {
    AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressResponse createAddress(AddressCreationRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner)
                .orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Address address = addressMapper.toAddress(request);
        address.setCustomer(owner);

//        addressResponse.setCustomer(owner);
        return  addressMapper.toAddressResponse(addressRepository.save(address));
    }

    public List<AddressResponse> getAll() {
        return addressMapper.toAddressResponses(addressRepository.findAll());
    }

    public AddressResponse getAddressById(Long id) {

        return addressMapper.toAddressResponse(addressRepository.findById(id).get());
    }

    public AddressResponse updateAddress(Long id, AddressUpdateRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(emailOwner)
                .orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.ADDRESS_NOT_FOUND));

        if(!address.getCustomer().equals(owner)) {
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        addressMapper.updateAddress(address, request);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }
    public void deleteAddress(Long id) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(emailOwner)
                .orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.ADDRESS_NOT_FOUND));

        if(!address.getCustomer().equals(owner)) {
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        addressRepository.delete(address);
    }
}
