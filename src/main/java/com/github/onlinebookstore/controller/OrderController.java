package com.github.onlinebookstore.controller;

import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.OrderHistoryDto;
import com.github.onlinebookstore.dto.order.OrderItemDto;
import com.github.onlinebookstore.dto.order.PatchOrderRequestDto;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.repository.OrderRepository;
import com.github.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create a new order")
    public OrderDto addOrder(@RequestBody CreateOrderRequestDto requestDto,
                             Authentication authentication) {
        return orderService.add(requestDto, getUserId(authentication));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get order history", description = "Get user's order history")
    public OrderHistoryDto getOrder(Authentication authentication) {
        return orderService.getOrderHistoryById(getUserId(authentication));
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get a specific order by id",
            description = "Get a specific order by id. Users can see only their orders")
    public OrderDto getOrderById(@PathVariable @Positive Long orderId,
                                 Authentication authentication) {
        return orderService.getOrderById(orderId, getUserId(authentication));
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get a specific item in a specific order",
            description = "Get a specific item in a specific order by their ids. "
                    + "Users can see only their orders")
    public OrderItemDto getOrderItemById(@PathVariable @Positive Long orderId,
                                         @PathVariable @Positive Long itemId,
                                         Authentication authentication) {
        return orderService.getOrderItemById(orderId, itemId, getUserId(authentication));
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order's status")
    public OrderDto updateOrder(@RequestBody PatchOrderRequestDto requestDto,
                                @PathVariable @Positive Long orderId) {
        return orderService.updateOrder(requestDto, orderId);
    }

    private Long getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
