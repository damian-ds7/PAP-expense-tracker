package com.example.expenseapi.pojo;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class Group extends BaseGroup{
    public Group(String name) {
        super(name);
    }
}
