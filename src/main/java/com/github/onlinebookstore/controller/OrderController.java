package com.github.onlinebookstore.controller;

import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.UpdateOrderRequestDto;
import com.github.onlinebookstore.dto.orderitem.OrderItemDto;
import com.github.onlinebookstore.model.User;
import com.github.onlinebookstore.service.OrderItemService;
import com.github.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "Orders", description = "Operations related to orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create a new order")
    public OrderDto createOrder(@RequestBody CreateOrderRequestDto requestDto,
                                Authentication authentication) {
        return orderService.add(requestDto, getUserId(authentication));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get order history", description = "Get user's order history")
    public List<OrderDto> getAll(Authentication authentication, Pageable pageable) {
        return orderService.getAllOrders(getUserId(authentication), pageable);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get a specific order by id",
            description = "Get a specific order by id. Users can see only their orders")
    public List<OrderItemDto> findItemsByOrder(@PathVariable @Positive Long orderId,
                                           Authentication authentication) {
        return orderItemService.findItemsByOrder(orderId, getUserId(authentication));
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get a specific item in a specific order",
            description = "Get a specific item in a specific order by their ids. "
                    + "Users can see only their orders")
    public OrderItemDto getOrderItemById(@PathVariable @Positive Long orderId,
                                         @PathVariable @Positive Long itemId,
                                         Authentication authentication) {
        return orderItemService.getOrderItemById(orderId, itemId, getUserId(authentication));
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order's status")
    public OrderDto updateOrder(@RequestBody @Valid UpdateOrderRequestDto requestDto,
                                @PathVariable @Positive Long orderId) {
        return orderService.updateOrder(requestDto, orderId);
    }

    private Long getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
