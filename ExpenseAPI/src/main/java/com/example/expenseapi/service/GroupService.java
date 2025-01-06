package com.example.expenseapi.service;

import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.Group;

import java.util.List;

public interface GroupService extends GenericService<Group, Long> {
    List<BaseGroup> getBaseGroups();
    List<Group> getActiveGroups();
}