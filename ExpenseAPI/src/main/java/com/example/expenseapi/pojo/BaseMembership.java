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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_membership_gen")
    @SequenceGenerator(
            name = "base_membership_gen",
            sequenceName = "base_membership_seq",
            allocationSize = 1
    )

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
