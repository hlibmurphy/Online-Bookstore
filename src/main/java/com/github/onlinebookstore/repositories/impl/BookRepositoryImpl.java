package com.github.onlinebookstore.repositories.impl;

import com.github.onlinebookstore.models.Book;
import com.github.onlinebookstore.repositories.BookRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }
}
