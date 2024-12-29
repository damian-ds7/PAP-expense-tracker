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
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR2(255)")
    private String name;

    @NonNull
    @Column(name = "surname", nullable = false, columnDefinition = "VARCHAR2(255)")
    private String surname;

    @NonNull
    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR2(255)")
    private String email;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @NonNull
    @Column(nullable = false)
    private String password;
}
