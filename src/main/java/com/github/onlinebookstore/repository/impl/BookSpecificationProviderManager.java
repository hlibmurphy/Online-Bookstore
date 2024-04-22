package com.github.onlinebookstore.repository.impl;

import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.repository.specifications.SpecificationProvider;
import com.github.onlinebookstore.repository.specifications.SpecificationProviderManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find correct specification "
                        + "provider or key " + key));
    }
}
