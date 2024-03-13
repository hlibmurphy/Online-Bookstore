package com.github.onlinebookstore.dto;

import lombok.Data;

@Data
public class BookSearchParameters {
    private String[] titles;
    private String[] authors;
    private String[] isbn;
}
