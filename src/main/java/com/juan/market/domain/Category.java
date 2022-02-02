package com.juan.market.domain;

import lombok.Data;

@Data
public class Category {

    private Long categoryId;
    private String category;
    private boolean active;
}
