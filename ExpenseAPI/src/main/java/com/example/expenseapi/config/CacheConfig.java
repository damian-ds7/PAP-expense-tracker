package com.example.expenseapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.example.expenseapi.utils.AuthHelper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "ExpenseID",
                "expenses",
                "expensesPage",
                "expInfoGroup",
                "expInfoAllGroups",
                "expensesMap",
                "recentExpense",
                "groupExpenseDateMap",
                "groupExpenseCategoryMap",
                "searchExpensesDTO",
                "searchExpensesPagesDTO",
                "expensesUserPage",
                "categories",
                "currencies",
                "methods",
                "baseGroups",
                "activeGroups",
                "membershipsByUserId",
                "admins",
                "users",
                "roles",
                "temporaryMembershipsByUserId",
                "temporaryMembershipsBySenderId"
        );
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .maximumSize(2000)
        );
        return cacheManager;
    }

    @Bean
    public KeyGenerator userBasedKeyGenerator() {
        return (target, method, params) -> AuthHelper.getUser().getId();
    }
}
