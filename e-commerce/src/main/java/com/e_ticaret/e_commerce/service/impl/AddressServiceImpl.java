package com.e_ticaret.e_commerce.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.AddressDto;
import com.e_ticaret.e_commerce.entity.Address;
import com.e_ticaret.e_commerce.entity.User;
import com.e_ticaret.e_commerce.repository.AddressRepository;
import com.e_ticaret.e_commerce.repository.UserRepository;
import com.e_ticaret.e_commerce.service.AddressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public void addAddress(AddressDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        Address address = Address.builder()
                .city(dto.getCity())
                .district(dto.getDistrict())
                .street(dto.getStreet())
                .postalCode(dto.getPostalCode())
                .user(user)
                .build();

        addressRepository.save(address);
    }

    @Override
    public List<AddressDto> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(a -> AddressDto.builder()
                        .city(a.getCity())
                        .district(a.getDistrict())
                        .street(a.getStreet())
                        .postalCode(a.getPostalCode())
                        .build())
                .collect(Collectors.toList());
    }
}
