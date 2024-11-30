package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
