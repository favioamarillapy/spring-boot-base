package com.py.konecta.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {

    private List<T> content;
    private int totalPages;
    private int size;
    private Long totalElements;
}
