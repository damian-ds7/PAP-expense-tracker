package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.Membership;
import org.springframework.data.repository.CrudRepository;

public interface MembershipRepository extends CrudRepository<Membership, Long> {}
