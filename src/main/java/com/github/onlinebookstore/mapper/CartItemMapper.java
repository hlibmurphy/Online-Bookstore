package com.github.onlinebookstore.mapper;

import com.github.onlinebookstore.config.MapperConfig;
import com.github.onlinebookstore.dto.cartitem.CartItemDto;
import com.github.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toModel(CreateCartItemRequestDto requestDto);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @AfterMapping
    default void setBookDetails(
            @MappingTarget CartItem cartItem,
            CreateCartItemRequestDto requestDto) {
        Book book = new Book();
        book.setId(requestDto.getBookId());
        cartItem.setBook(book);
    }
}
