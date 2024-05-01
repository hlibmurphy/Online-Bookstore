package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.UpdateOrderRequestDto;
import com.github.onlinebookstore.mapper.OrderItemMapper;
import com.github.onlinebookstore.mapper.OrderMapper;
import com.github.onlinebookstore.model.Order;
import com.github.onlinebookstore.model.OrderItem;
import com.github.onlinebookstore.model.ShoppingCart;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.OrderItemRepository;
import com.github.onlinebookstore.repository.OrderRepository;
import com.github.onlinebookstore.repository.ShoppingCartRepository;
import com.github.onlinebookstore.repository.UserRepository;
import com.github.onlinebookstore.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderDto add(CreateOrderRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        Set<OrderItem> orderItems = orderItemMapper.toOrderItems(shoppingCart.getCartItems());
        Order order = orderMapper.toModel(requestDto);
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setOrderItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        User user = getUserById(userId);
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setTotal(order.getOrderItems().stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAllByUserId(userId, pageable);
        List<Order> orders = ordersPage.getContent();
        return orderMapper.toDtos(orders);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(UpdateOrderRequestDto requestDto, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with id " + orderId + " not found")
        );
        order.setStatus(requestDto.getStatus());
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found")
        );
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("No shopping cart owned by user with id "
                                + userId + " found"));
    }
}
