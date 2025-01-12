package com.example.expenseapi.specification;

import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.TemporaryMembership;
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
        return (root, query, criteriaBuilder) -> {
            if (groupName == null || query == null) {
                return criteriaBuilder.conjunction();
            }
            Subquery<Long> membershipSubquery = query.subquery(Long.class);
            Root<Membership> membershipRoot = membershipSubquery.from(Membership.class);
            Join<Membership, Group> membershipGroupJoin = membershipRoot.join("group", JoinType.INNER);

            membershipSubquery.select(membershipRoot.get("user").get("id"))
                    .where(
                            criteriaBuilder.equal(membershipGroupJoin.get("name"), groupName),
                            criteriaBuilder.equal(membershipRoot.get("user").get("id"), root.get("id"))
                    );
            Subquery<Long> temporaryMembershipSubquery = query.subquery(Long.class);
            Root<TemporaryMembership> temporaryMembershipRoot = temporaryMembershipSubquery.from(TemporaryMembership.class);
            Join<TemporaryMembership, Group> temporaryGroupJoin = temporaryMembershipRoot.join("group", JoinType.INNER);

            temporaryMembershipSubquery.select(temporaryMembershipRoot.get("user").get("id"))
                    .where(
                            criteriaBuilder.equal(temporaryGroupJoin.get("name"), groupName),
                            criteriaBuilder.equal(temporaryMembershipRoot.get("user").get("id"), root.get("id"))
                    );
            return criteriaBuilder.and(
                    criteriaBuilder.not(criteriaBuilder.exists(membershipSubquery)),
                    criteriaBuilder.not(criteriaBuilder.exists(temporaryMembershipSubquery))
            );
        };
    }

}
