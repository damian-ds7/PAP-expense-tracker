package com.example.expenseapi.pojo;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "preferences")
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Currency currency;

    @NonNull
    @JoinColumn(name = "method_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MethodOfPayment method;

    @NonNull
    @Column(name = "language", nullable = false)
    private String language = "pl";

    public Preference(@NonNull Currency currency, @NonNull MethodOfPayment method) {
        this.currency = currency;
        this.method = method;
    }

    public Preference(@NonNull User user, @NonNull Currency currency,@NonNull MethodOfPayment method, @NonNull String language) {
        this(currency, method);
        this.language = language;
    }
}
