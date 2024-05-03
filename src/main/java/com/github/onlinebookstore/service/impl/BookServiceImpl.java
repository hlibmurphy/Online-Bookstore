package com.github.onlinebookstore.service.impl;

import com.github.onlinebookstore.dto.book.BookDto;
import com.github.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.github.onlinebookstore.dto.book.BookSearchParameters;
import com.github.onlinebookstore.dto.book.CreateBookRequestDto;
import com.github.onlinebookstore.mapper.BookMapper;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.repository.BookRepository;
import com.github.onlinebookstore.repository.impl.BookSpecificationBuilder;
import com.github.onlinebookstore.service.BookService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return bookMapper.toDtos(books);
    }

    @Override
    public BookDto findBookById(Long id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return bookMapper.toDto(foundBook.orElseThrow(() -> new NoSuchElementException("No book "
                + "with id " + id + " found.")));
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId) {
        List<Book> allByCategoryId = bookRepository.findBookByCategoriesId(categoryId);
        return bookMapper.toDtosWithoutCategories(allByCategoryId);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookDto updateBook(Long id, CreateBookRequestDto newBookRequest) {
        bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No book with id " + id + " found."));
        Book newBook = bookMapper.toModel(newBookRequest);
        newBook.setId(id);
        Book savedBook = bookRepository.save(newBook);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> search(BookSearchParameters parameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookMapper.toDtos(bookRepository.findAll(bookSpecification, pageable));
    }
}
