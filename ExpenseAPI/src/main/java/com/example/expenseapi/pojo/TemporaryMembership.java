package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "temporary_memberships")
public class TemporaryMembership extends BaseMembership<Group> {
    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    public TemporaryMembership(User user, Group group, Role role, User sender) {
        super(user, group, role);
        this.sender = sender;
    }

    public TemporaryMembership(BaseMembership<Group> entity, User sender) {
        super(entity.user, entity.group, entity.role);
        this.sender = sender;
    }
}
