package com.e_ticaret.e_commerce.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.OrderItemRequestDto;
import com.e_ticaret.e_commerce.dto.OrderItemResponseDto;
import com.e_ticaret.e_commerce.dto.OrderRequestDto;
import com.e_ticaret.e_commerce.dto.OrderResponseDto;
import com.e_ticaret.e_commerce.entity.Order;
import com.e_ticaret.e_commerce.entity.OrderItem;
import com.e_ticaret.e_commerce.entity.Payment;
import com.e_ticaret.e_commerce.entity.Product;
import com.e_ticaret.e_commerce.entity.User;
import com.e_ticaret.e_commerce.repository.OrderItemRepository;
import com.e_ticaret.e_commerce.repository.OrderRepository;
import com.e_ticaret.e_commerce.repository.PaymentRepository;
import com.e_ticaret.e_commerce.repository.ProductRepository;
import com.e_ticaret.e_commerce.repository.UserRepository;
import com.e_ticaret.e_commerce.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId()).orElseThrow();

            if (product.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException(product.getName() + " ürününden yeterli stok yok.");
            }

            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            total = total.add(itemTotal);

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            items.add(item);
        }

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .customer(user)
                .items(items)
                .totalAmount(total)
                .shippingAddress(dto.getShippingAddress())
                .billingAddress(dto.getBillingAddress())
                .paymentMethod(dto.getPaymentMethod())
                .cardNumber(dto.getCardNumber())
                .build();

        orderRepository.save(order);
        items.forEach(i -> {
            i.setOrder(order);
            orderItemRepository.save(i);
        });

        // Stripe ödeme varsa kayıt oluştur
        if ("Kredi Kartı".equalsIgnoreCase(dto.getPaymentMethod()) && dto.getTransactionId() != null) {
            Payment payment = Payment.builder()
                    .amount(dto.getAmount())
                    .transactionId(dto.getTransactionId())
                    .paymentMethod("Kredi Kartı")
                    .order(order)
                    .paymentDate(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);
        }

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .createdAt(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .items(items.stream().map(i -> OrderItemResponseDto.builder()
                        .productName(i.getProduct().getName())
                        .quantity(i.getQuantity())
                        .price(i.getUnitPrice())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<OrderResponseDto> getOrdersByUser(Long userId) {
        return orderRepository.findByCustomerId(userId).stream().map(order ->
                OrderResponseDto.builder()
                        .orderId(order.getId())
                        .createdAt(order.getCreatedAt())
                        .totalAmount(order.getTotalAmount())
                        .shippingAddress(order.getShippingAddress())
                        .billingAddress(order.getBillingAddress())
                        .paymentMethod(order.getPaymentMethod())
                        .cardNumber(order.getCardNumber())
                        .items(order.getItems().stream().map(i -> OrderItemResponseDto.builder()
                                .productName(i.getProduct().getName())
                                .quantity(i.getQuantity())
                                .price(i.getUnitPrice())
                                .build()).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());
    }
}
