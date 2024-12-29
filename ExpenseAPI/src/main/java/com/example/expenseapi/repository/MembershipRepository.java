package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.BaseGroup;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MembershipRepository extends JpaRepository<Membership, Long> {
    @Query("SELECT m.group FROM Membership m WHERE m.user.id = :userId")
    List<BaseGroup> findBaseGroupsByUser_Id(@Param("userId") Long userId);

    @Query("select m.user from Membership m where m.role.name = 'admin' and m.group.name = :group")
    List<User> findAdmins(String group);

    List<Membership> findByUserId(Long userId);
}
