package com.github.onlinebookstore.repositories.specifications.book;

import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.repositories.specifications.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecification implements SpecificationProvider<Book> {
    private static final String KEY_NAME = "title";

    @Override
    public String getKey() {
        return KEY_NAME;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get("title")
                .in(Arrays.stream(params).toArray());
    }
}
