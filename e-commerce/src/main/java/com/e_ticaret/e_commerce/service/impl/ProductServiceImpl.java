package com.e_ticaret.e_commerce.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.ProductDto;
import com.e_ticaret.e_commerce.entity.Product;
import com.e_ticaret.e_commerce.entity.Seller;
import com.e_ticaret.e_commerce.repository.CategoryRepository;
import com.e_ticaret.e_commerce.repository.ProductRepository;
import com.e_ticaret.e_commerce.repository.SellerRepository;
import com.e_ticaret.e_commerce.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    @Override
public ProductDto createProduct(ProductDto productDto) {
    Seller seller = sellerRepository.findAll().stream()
        .filter(s -> s.getUser().getId().equals(productDto.getSellerId()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));

    Product product = Product.builder()
            .name(productDto.getName())
            .description(productDto.getDescription())
            .price(productDto.getPrice())
            .stock(productDto.getStock())
            .imageUrl(productDto.getImageUrl())
            .seller(seller)
            .category(categoryRepository.findById(productDto.getCategoryId())
                      .orElseThrow(() -> new RuntimeException("Kategori bulunamadı")))
                      
            .build();

    Product savedProduct = productRepository.save(product);

    return ProductDto.builder()
            .id(savedProduct.getId())
            .name(savedProduct.getName())
            .description(savedProduct.getDescription())
            .price(savedProduct.getPrice())
            .stock(savedProduct.getStock())
            .imageUrl(savedProduct.getImageUrl())
            .sellerId(savedProduct.getSeller().getId())
            .categoryId(savedProduct.getCategory().getId())
            .categoryName(savedProduct.getCategory().getName())
            .build();
}


        @Override
        public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .sellerId(product.getSeller().getId())
                .categoryId(product.getCategory().getId())
                .build();
    }
    @Override
        public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());

        productRepository.save(product);

        return dto;
        }

        @Override
        public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        if (!product.getOrderItems().isEmpty()) {
                throw new RuntimeException("Bu ürün sipariş geçmişine sahip ve silinemez.");
        }

        productRepository.deleteById(id);
        }

        
        @Override
        public void forceDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        // İlişkili order item'ları sil
        product.getOrderItems().forEach(item -> item.setProduct(null));

        productRepository.delete(product);
        }

        
        @Override
        public List<ProductDto> getProductsBySeller(Long sellerId) {
        List<Product> products = productRepository.findBySellerId(sellerId);
        return products.stream().map(this::mapToDto).toList();
        }

        private ProductDto mapToDto(Product product) {
                return ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .imageUrl(product.getImageUrl())
                        .sellerId(product.getSeller() != null ? product.getSeller().getId() : null)
                        .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                        .categoryName(product.getCategory().getName())
                        .build();
        }

        @Override
        public Product getEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        }


        

}
