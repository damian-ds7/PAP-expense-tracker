package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "memberships")
public class Membership extends BaseMembership{
    public Membership(User user, BaseGroup group, Role role){
        super(user, group, role);
    }

    public Membership(TemporaryMembership temporaryMembership) {
        super(temporaryMembership.getUser(), temporaryMembership.getGroup(), temporaryMembership.getRole());
    }
}
