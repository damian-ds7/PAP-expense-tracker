package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Preference;
import com.example.expenseapi.repository.PreferenceRepository;

public class PreferenceServiceImpl extends GenericServiceImpl<Preference, Long> implements PreferenceService {
    public PreferenceServiceImpl(PreferenceRepository repository) {
        super(repository);
    }
}
