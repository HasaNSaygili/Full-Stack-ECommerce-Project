package com.e_ticaret.e_commerce.service;


import java.util.List;

import com.e_ticaret.e_commerce.dto.ProductDto;
import com.e_ticaret.e_commerce.entity.Product;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto updateProduct(Long id, ProductDto dto);
    void deleteProduct(Long id);
    List<ProductDto> getProductsBySeller(Long sellerId);
    Product getEntityById(Long id);
    void forceDeleteProduct(Long id);

}
