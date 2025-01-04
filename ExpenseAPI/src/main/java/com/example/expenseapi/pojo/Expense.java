package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    private double price;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "membership_id", referencedColumnName = "id")
    private Membership membership;

    @Column(name = "expense_date", nullable = false)
    private LocalDate date = LocalDate.now();

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "method_id", referencedColumnName = "id")
    private MethodOfPayment method;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    public Expense(@NonNull String title, double price, @NonNull Membership membership, @NonNull Category category, @NonNull Currency currency, @NonNull MethodOfPayment method) {
        this.title = title;
        this.price = price;
        this.membership = membership;
        this.category = category;
        this.currency = currency;
        this.method = method;
    }

    public Expense(String title, double price, @NonNull Membership membership, Category category, LocalDate date, Currency currency, MethodOfPayment method) {
        this(title, price, membership, category, currency, method);
        this.setDate(date);
    }
}
