package com.mat.hexa_finance.application.ports.in.category.command;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateCategoryCommand{
    private long id;
    private String name;
    private String iconName;
    private Long userId;

    public UpdateCategoryCommand(long id, String name, String iconName, Long userId) {
        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.userId = userId;
    }
}
