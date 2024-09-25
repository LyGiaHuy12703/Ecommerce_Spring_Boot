package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.AddressCreationRequest;
import com.minhbui.ecommerce.dto.request.AddressUpdateRequest;
import com.minhbui.ecommerce.dto.response.AddressResponse;
import com.minhbui.ecommerce.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressCreationRequest request);
    AddressResponse toAddressResponse(Address address);
    List<AddressResponse> toAddressResponses(List<Address> addresses);
    void updateAddress(@MappingTarget Address address, AddressUpdateRequest request);
}
