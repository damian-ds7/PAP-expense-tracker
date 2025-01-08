package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "currencies", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"symbol"})
})
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "symbol", nullable = false)
    private String symbol = "PLN";

    @NonNull
    @Column(name = "exchange_rate", nullable = false)
    private double exchangeRate = 1.0;

    public Currency(@NonNull String symbol, double exchangeRate) {
        this.symbol = symbol;
        this.exchangeRate = exchangeRate;
    }
}
