package com.e_ticaret.e_commerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    private String city;
    private String district;
    private String street;
    private String postalCode;
}
