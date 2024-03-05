package com.github.onlinebookstore.services;

import com.github.onlinebookstore.dto.BookDto;
import com.github.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findBookById(Long id);
}
