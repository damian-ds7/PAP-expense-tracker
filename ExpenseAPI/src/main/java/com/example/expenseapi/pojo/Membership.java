package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "memberships")
public class Membership extends BaseMembership{
    public Membership(User user, BaseGroup group, String name, Role role){
        super(user, group, name, role);
    }

    public Membership(TemporaryMembership temporaryMembership) {
        super(temporaryMembership.getUser(), temporaryMembership.getGroup(), temporaryMembership.getName(), temporaryMembership.getRole());
    }
}
