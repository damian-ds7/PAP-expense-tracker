package com.example.expenseapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching  // Włącza mechanizm cache'owania w Spring
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
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
                "categoriesAll"
        );
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .maximumSize(2000)
        );
        return cacheManager;
    }
}
