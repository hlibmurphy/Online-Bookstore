package com.github.onlinebookstore.services.impl;

import com.github.onlinebookstore.dto.BookDto;
import com.github.onlinebookstore.dto.CreateBookRequestDto;
import com.github.onlinebookstore.mapper.BookMapper;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.repositories.BookRepository;
import com.github.onlinebookstore.services.BookService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findBookById(Long id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return bookMapper.toDto(foundBook.orElseThrow(() -> new NoSuchElementException("No book "
                + "with id " + id + " found.")));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto newBookRequest) {
        bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No book with id " + id + " found."));
        Book newBook = bookMapper.toModel(newBookRequest);
        newBook.setId(id);
        Book savedBook = bookRepository.save(newBook);
        return bookMapper.toDto(savedBook);
    }
}
