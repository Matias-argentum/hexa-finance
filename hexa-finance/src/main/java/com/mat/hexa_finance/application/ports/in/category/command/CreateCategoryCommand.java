package com.mat.hexa_finance.application.ports.in.category.command;

import lombok.Getter;
import lombok.Value;

@Value @Getter
public class CreateCategoryCommand {
    private String name;
    private String iconName;
    private Long userId;
}
