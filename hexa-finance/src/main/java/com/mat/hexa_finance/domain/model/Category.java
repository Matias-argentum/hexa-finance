package com.mat.hexa_finance.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Category {
    private Long id;
    private String name;
    private String iconName;
    private Long userId;

    public Category(Long id, String name, String iconName, Long userId) {
        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.userId = userId;
    }
}
