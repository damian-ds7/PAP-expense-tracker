package com.example.expenseapi.utils;

import com.example.expenseapi.pojo.Expense;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class ExpenseSpecification {
    public static Specification<Expense> hasCategory(List<String> categoryNames) {
        return (root, query, criteriaBuilder) -> {
            if (categoryNames == null || categoryNames.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("category").get("name").in(categoryNames);
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

    public static Specification<Expense> hasMethod(List<String> methods) {
        return (root, query, cb) -> {
            if (methods == null || methods.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("method").get("name").in(methods);
        };
    }

    public static Specification<Expense> hasGroup(String groupName) {
        return (root, query, cb) -> {
            if (groupName == null || groupName.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("membership").get("group").get("name"), groupName);
        };
    }

    public static Specification<Expense> isUser(String email) {
        return (root, query, cb) -> {
            if (email == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("membership").get("user").get("email"), email);
        };
    }

    public static Specification<Expense> isUserInList(List<String> emails) {
        return (root, query, cb) -> {
            if (emails == null || emails.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("membership").get("user").get("email").in(emails);
        };
    }

}
