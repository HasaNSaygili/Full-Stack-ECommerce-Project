package com.e_ticaret.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_ticaret.e_commerce.dto.ProductDto;
import com.e_ticaret.e_commerce.entity.Product;
import com.e_ticaret.e_commerce.entity.Seller;
import com.e_ticaret.e_commerce.repository.SellerRepository;
import com.e_ticaret.e_commerce.security.JwtUtil;
import com.e_ticaret.e_commerce.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;
    private final SellerRepository sellerRepository;


     @PostMapping
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto, HttpServletRequest request) {
    String token = request.getHeader("Authorization").substring(7);
    Long userId = jwtUtil.extractUserId(token); // Bunu JwtUtil içinde zaten yazmıştık
    dto.setSellerId(userId); // frontend'den gelen sellerId'yi güvence altına al
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
}


    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts(); 
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
        public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
            return ResponseEntity.ok(productService.updateProduct(id, dto));
        }

    @DeleteMapping("/{id}")
@PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
    String token = request.getHeader("Authorization").substring(7);
    Long userId = jwtUtil.extractUserId(token);
    String role = jwtUtil.extractRole(token);

    Product product = productService.getEntityById(id);
    Long sellerUserId = product.getSeller().getUser().getId();

    // Yetkisizse 403 dön
    if (!userId.equals(sellerUserId) && !role.equals("ROLE_ADMIN")) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
}



    @GetMapping("/seller/{sellerId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductDto>> getProductsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductDto>> getMyProducts(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long sellerUserId = jwtUtil.extractUserId(token);
        Seller seller = sellerRepository.findAll().stream()
            .filter(s -> s.getUser().getId().equals(sellerUserId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
            
        return ResponseEntity.ok(productService.getProductsBySeller(seller.getId()));
    }


    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminDeleteProduct(@PathVariable Long id) {
        productService.forceDeleteProduct(id);
        return ResponseEntity.ok("Ürün zorla silindi.");
    }

    

}
