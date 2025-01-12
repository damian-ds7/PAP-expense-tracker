package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseMembership<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    protected User user;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    protected T group;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    protected Role role;

    public BaseMembership(User user, T group, Role role) {
        this.user = user;
        this.group = group;
        this.role = role;
    }




}
