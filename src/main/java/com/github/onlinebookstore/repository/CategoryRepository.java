package com.github.onlinebookstore.repository;

import com.github.onlinebookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
