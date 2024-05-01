package com.github.onlinebookstore.mapper;

import com.github.onlinebookstore.config.MapperConfig;
import com.github.onlinebookstore.dto.orderitem.OrderItemDto;
import com.github.onlinebookstore.dto.orderitem.OrderItemDtoWithoutOrder;
import com.github.onlinebookstore.model.CartItem;
import com.github.onlinebookstore.model.OrderItem;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDtoWithoutOrder toDtoWithoutOrder(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "book.price")
    OrderItem toOrderItem(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "orderId", source = "order.id")
    OrderItemDto toDto(OrderItem orderItem);

    default Set<OrderItem> toOrderItems(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
    }

    default List<OrderItemDto> toDtos(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .toList();
    }
}
