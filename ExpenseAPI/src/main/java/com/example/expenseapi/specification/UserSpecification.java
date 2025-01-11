package com.example.expenseapi.specification;

import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> nameContains(String name) {
        return ((root, query, criteriaBuilder) -> {
            if (name == null) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + name.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern);
        });
    }

    public static Specification<User> surnameContains(String surname) {
        return (((root, query, criteriaBuilder) -> {
            if (surname == null) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + surname.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), pattern);
        }));
    }

    public static Specification<User> notInGroup(String groupName) {
        return ((root, query, criteriaBuilder) -> {
            if (groupName == null || query == null) {
                return criteriaBuilder.conjunction();
            }
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Membership> membershipRoot = subquery.from(Membership.class);
            Join<Membership, Group> groupJoin = membershipRoot.join("group", JoinType.INNER);

            subquery.select(membershipRoot.get("user").get("id"))
                    .where(
                            criteriaBuilder.equal(groupJoin.get("name"), groupName),
                            criteriaBuilder.equal(membershipRoot.get("user").get("id"), root.get("id"))
                    );
            return criteriaBuilder.not(criteriaBuilder.exists(subquery));

        }
        );
    }
}
