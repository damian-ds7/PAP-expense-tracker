package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_group_gen")
    @SequenceGenerator(
            name = "base_group_gen",
            sequenceName = "base_group_seq",
            allocationSize = 1
    )
    protected Long id;

    @Column(name = "name")
    protected String name;

    public BaseGroup(String name) {
        this.name = name;
    }
}
