package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.OrderHistoryDto;
import com.github.onlinebookstore.dto.order.OrderItemDto;
import com.github.onlinebookstore.dto.order.PatchOrderRequestDto;
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
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
        Set<OrderItem> orderItems = orderItemMapper.toOrderItem(shoppingCart.getCartItems());
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
    public OrderHistoryDto getOrderHistoryById(Long userId) {
        List<OrderDto> orderDtos = orderMapper.toDto(orderRepository.findAllByUserId(userId));
        return orderMapper.toHistoryDto(orderDtos);
    }

    @Override
    public OrderDto getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with userId " + userId + " not found")
        );

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User "
                    + userId
                    + " is not authorized to access this order");
        }

        return orderMapper.toDto(order);
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository.findById(itemId).orElseThrow(
                () -> new EntityNotFoundException("Order item with itemId " + itemId + " not found")
        );

        if (!Objects.equals(orderItem.getOrder().getId(), orderId)) {
            throw new EntityNotFoundException("Order with id " + orderId
                    + " not found in order " + "with id " + itemId);
        }

        if (!orderItem.getOrder().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User "
                    + userId
                    + " is not authorized to access this order");
        }

        return orderItemMapper.toDto(orderItem);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(PatchOrderRequestDto requestDto, Long orderId) {
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
