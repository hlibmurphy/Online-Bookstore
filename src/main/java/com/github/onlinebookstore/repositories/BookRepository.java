package com.github.onlinebookstore.repositories;

import com.github.onlinebookstore.models.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
