package com.github.onlinebookstore.service;

import com.github.onlinebookstore.dto.book.BookDto;
import com.github.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.github.onlinebookstore.dto.book.BookSearchParameters;
import com.github.onlinebookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findBookById(Long id);

    List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId);

    BookDto deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters parameters, Pageable pageable);
}
