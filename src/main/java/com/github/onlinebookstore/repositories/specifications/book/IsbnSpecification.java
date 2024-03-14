package com.github.onlinebookstore.repositories.specifications.book;

import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.repositories.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecification implements SpecificationProvider<Book> {
    private static final String KEY_NAME = "isbn";

    @Override
    public String getKey() {
        return KEY_NAME;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get("isbn")
                .in(Arrays.stream(params).toArray());
    }
}
