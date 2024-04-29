package com.github.onlinebookstore.mapper;

import com.github.onlinebookstore.config.MapperConfig;
import com.github.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.github.onlinebookstore.dto.order.OrderDto;
import com.github.onlinebookstore.dto.order.OrderHistoryDto;
import com.github.onlinebookstore.model.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toModel(CreateOrderRequestDto requestDto);

    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);

    @Mapping(target = "userId", source = "user.id")
    List<OrderDto> toDto(List<Order> order);

    default OrderHistoryDto toHistoryDto(List<OrderDto> orderDtos) {
        OrderHistoryDto historyDto = new OrderHistoryDto();
        historyDto.setOrders(orderDtos);
        return historyDto;
    }
}
