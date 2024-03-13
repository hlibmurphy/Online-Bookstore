package com.github.onlinebookstore.repositories.impl;

import com.github.onlinebookstore.dto.BookSearchParameters;
import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.repositories.SpecificationBuilder;
import com.github.onlinebookstore.repositories.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (searchParameters == null) {
            return specification;
        }

        if (searchParameters.getTitles() != null && searchParameters.getTitles().length > 0) {
            specification =
                    specification.and(bookSpecificationProviderManager.getSpecificationProvider(
                            "title").getSpecification(searchParameters.getTitles()));
        }

        if (searchParameters.getAuthors() != null && searchParameters.getAuthors().length > 0) {
            specification =
                    specification.and(bookSpecificationProviderManager.getSpecificationProvider(
                            "author").getSpecification(searchParameters.getAuthors()));
        }

        if (searchParameters.getIsbn() != null && searchParameters.getIsbn().length > 0) {
            specification =
                    specification.and(bookSpecificationProviderManager.getSpecificationProvider(
                            "isbn").getSpecification(searchParameters.getIsbn()));
        }

        return specification;
    }
}
