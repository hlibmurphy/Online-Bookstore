package com.github.onlinebookstore.controller;

import com.github.onlinebookstore.dto.BookDto;
import com.github.onlinebookstore.dto.CreateBookRequestDto;
import com.github.onlinebookstore.services.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PostMapping("/books")
    public BookDto createBok(@RequestBody CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }
}
