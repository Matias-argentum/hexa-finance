package com.mat.hexa_finance.infrastructure.adapters.out.persistence.category;

import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String iconName;

    // saco el user id y pongo el objeto completo de enrridad de User para establecer la relacion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity owner;
}
