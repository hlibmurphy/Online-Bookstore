package com.github.onlinebookstore.repository.specifications;

import com.github.onlinebookstore.dto.book.BookSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters searchParameters);
}
