package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "methods_of_payment", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class MethodOfPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "name", nullable = false)
    private String name = "cash";

    public MethodOfPayment(String name) {
        this.name = name;
    }
}


