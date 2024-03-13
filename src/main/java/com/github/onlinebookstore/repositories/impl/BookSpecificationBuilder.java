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
    private static final String TITLE_KEY = "title";
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
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
                            TITLE_KEY).getSpecification(searchParameters.getTitles()));
        }

        if (searchParameters.getAuthors() != null && searchParameters.getAuthors().length > 0) {
            specification =
                    specification.and(bookSpecificationProviderManager.getSpecificationProvider(
                            AUTHOR_KEY).getSpecification(searchParameters.getAuthors()));
        }

        if (searchParameters.getIsbn() != null && searchParameters.getIsbn().length > 0) {
            specification =
                    specification.and(bookSpecificationProviderManager.getSpecificationProvider(
                            ISBN_KEY).getSpecification(searchParameters.getIsbn()));
        }

        return specification;
    }
}
