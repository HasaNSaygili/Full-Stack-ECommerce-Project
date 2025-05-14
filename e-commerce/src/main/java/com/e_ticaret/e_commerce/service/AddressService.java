package com.e_ticaret.e_commerce.service;

import java.util.List;

import com.e_ticaret.e_commerce.dto.AddressDto;

public interface AddressService {
    void addAddress(AddressDto addressDto, Long userId);
    List<AddressDto> getUserAddresses(Long userId);
}
