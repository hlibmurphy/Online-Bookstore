package com.github.onlinebookstore.mapper;

import com.github.onlinebookstore.config.MapperConfig;
import com.github.onlinebookstore.dto.book.BookDto;
import com.github.onlinebookstore.dto.book.CreateBookRequestDto;
import com.github.onlinebookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
