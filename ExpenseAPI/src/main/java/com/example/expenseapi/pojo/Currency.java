package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name = "Zlotowka";

    @NonNull
    @Column(name = "symbol", nullable = false)
    private String symbol = "PLN";

    @NonNull
    @Column(name = "exchange_rate", nullable = false)
    private double exchangeRate = 1.0;

    public Currency(@NonNull String name, @NonNull String symbol, double exchangeRate) {
        this.name = name;
        this.symbol = symbol;
        this.exchangeRate = exchangeRate;
    }
}
