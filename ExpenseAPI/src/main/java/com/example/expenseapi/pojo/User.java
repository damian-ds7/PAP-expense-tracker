package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @NonNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @NonNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @NonNull
    @JoinColumn(name = "preferences_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Preference preference;

    @NonNull
    @Column(nullable = false)
    private String password;
}
