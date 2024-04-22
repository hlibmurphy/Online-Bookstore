package com.github.onlinebookstore.mapper;

import com.github.onlinebookstore.config.MapperConfig;
import com.github.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.github.onlinebookstore.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toDto(ShoppingCart cart);

    @AfterMapping
    default void toDto(@MappingTarget ShoppingCartResponseDto dto, ShoppingCart cart) {
        dto.setUserId(cart.getUser().getId());
    }
}
