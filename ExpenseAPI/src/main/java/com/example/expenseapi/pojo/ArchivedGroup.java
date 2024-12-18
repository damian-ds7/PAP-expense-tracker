package com.example.expenseapi.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "archived_groups")
public class ArchivedGroup extends BaseGroup{
    public ArchivedGroup(String name) {
        super(name);
    }
}
