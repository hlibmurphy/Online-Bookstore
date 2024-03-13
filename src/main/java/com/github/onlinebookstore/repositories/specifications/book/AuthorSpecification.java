package com.github.onlinebookstore.repositories.specifications.book;

import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.repositories.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecification implements SpecificationProvider<Book> {
    private static final String KEY_NAME = "author";

    @Override
    public String getKey() {
        return KEY_NAME;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get("author")
                .in(Arrays.stream(params).toArray());
    }
}
