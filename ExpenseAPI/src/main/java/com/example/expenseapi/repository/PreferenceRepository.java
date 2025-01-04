package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {}
