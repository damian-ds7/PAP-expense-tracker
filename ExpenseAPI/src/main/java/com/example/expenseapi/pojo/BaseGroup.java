package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_group_gen")
    @SequenceGenerator(
            name = "base_group_gen",
            sequenceName = "base_group_seq",
            allocationSize = 1
    )
    protected Long id;

    @NonNull
    @NotBlank
    @Column(name = "name", nullable = false)
    protected String name;

    public BaseGroup(@NonNull String name) {
        this.name = name;
    }
}
