package com.e_ticaret.e_commerce.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    private Long id;

    @NotBlank(message = "Ürün adı boş olamaz.")
    private String name;

    private String description;

    @NotNull(message = "Fiyat boş olamaz.")
    private BigDecimal price;

    @NotNull(message = "Stok bilgisi boş olamaz.")
    private Integer stock;

    private String imageUrl;

    @NotNull(message = "Satıcı ID boş olamaz.")
    private Long sellerId;

    @NotNull(message = "Kategori ID boş olamaz.")
    private Long categoryId;

    @NotNull(message = "Kategori ismi boş olamaz.")
    private String categoryName;
}
