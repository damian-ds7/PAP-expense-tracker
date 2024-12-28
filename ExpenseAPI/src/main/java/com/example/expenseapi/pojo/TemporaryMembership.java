package com.example.expenseapi.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "temporary_memberships")
public class TemporaryMembership extends BaseMembership {
    public TemporaryMembership(User user, BaseGroup group, String name, Role role) {
        super(user, group, name, role);
    }
}
