package com.github.onlinebookstore.repositories.specifications;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
