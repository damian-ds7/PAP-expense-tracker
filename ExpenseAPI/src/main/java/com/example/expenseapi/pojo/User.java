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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR2(255)")
    private String name;

    @Column(name = "surname", nullable = false, columnDefinition = "VARCHAR2(255)")
    private String surname;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR2(255)")
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public User(String username, String surname, String email, Group group) {
        this.name = username;
        this.surname = surname;
        this.email = email;
        this.group = group;
    }
}
