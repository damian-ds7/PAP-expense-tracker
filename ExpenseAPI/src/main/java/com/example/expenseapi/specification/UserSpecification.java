package com.example.expenseapi.specification;

import com.example.expenseapi.pojo.Group;
import com.example.expenseapi.pojo.Membership;
import com.example.expenseapi.pojo.TemporaryMembership;
import com.example.expenseapi.pojo.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> nameMatches(String name, int maxDistance) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.disjunction();
            }
            Expression<Integer> editDistance = criteriaBuilder.function(
                    "UTL_MATCH.edit_distance",
                    Integer.class,
                    criteriaBuilder.lower(root.get("name")),
                    criteriaBuilder.literal(name.toLowerCase())
            );
            return criteriaBuilder.lessThanOrEqualTo(editDistance, maxDistance);
        };
    }

    public static Specification<User> surnameMatches(String surname, int maxDistance) {
        return (root, query, criteriaBuilder) -> {
            if (surname == null || surname.trim().isEmpty()) {
                return criteriaBuilder.disjunction();
            }
            Expression<Integer> editDistance = criteriaBuilder.function(
                    "UTL_MATCH.edit_distance",
                    Integer.class,
                    criteriaBuilder.lower(root.get("surname")),
                    criteriaBuilder.literal(surname.toLowerCase())
            );
            return criteriaBuilder.lessThanOrEqualTo(editDistance, maxDistance);
        };
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
