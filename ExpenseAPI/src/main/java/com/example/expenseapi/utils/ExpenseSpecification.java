package com.example.expenseapi.utils;

import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.Membership;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ExpenseSpecification {
    public static Specification<Expense> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category").get("name"), categoryName);
        };
    }

    public static Specification<Expense> dateIs(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("date"), date);
        };
    }

    public static Specification<Expense> dateBetween(LocalDate begin, LocalDate end) {
        return (root, query, cb) -> {
            if (begin == null && end == null) {
                return cb.conjunction();
            }
            if (begin != null && end != null) {
                return cb.between(root.get("date"), begin, end);
            } else if (begin != null) {
                return cb.greaterThanOrEqualTo(root.get("date"), begin);
            } else {
                return cb.lessThanOrEqualTo(root.get("date"), end);
            }
        };
    }

    public static Specification<Expense> priceBetween(Double left, Double right) {
        return (root, query, cb) -> {
            if (left == null && right == null) {
                return cb.conjunction();
            }
            if (left != null && right != null) {
                return cb.between(root.get("price"), left, right);
            } else if (left != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), left);
            } else {
                return cb.lessThanOrEqualTo(root.get("price"), right);
            }
        };
    }

    public static Specification<Expense> priceLessThan(Double price) {
        return (root, query, cb) -> {
            if (price == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Expense> priceGreaterThan(Double price) {
        return (root, query, cb) -> {
            if (price == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Expense> hasGroup(String groupName) {
        return (root, query, cb) -> {
            if (groupName == null || groupName.isBlank()) {
                return cb.conjunction();
            }
            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<Membership> membershipRoot = subQuery.from(Membership.class);
            subQuery.select(membershipRoot.get("user").get("id"));
            subQuery.where(
                    cb.equal(membershipRoot.join("group").get("name"), groupName)
            );
            return cb.in(root.get("user").get("id")).value(subQuery);
        };
    }

    public static Specification<Expense> isUser(String email) {
        return (root, query, cb) -> {
            if (email == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user").get("email"), email);
        };
    }

}
