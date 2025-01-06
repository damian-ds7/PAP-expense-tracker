package com.example.expenseapi.service;

import com.example.expenseapi.dto.PreferenceUpdateDTO;
import com.example.expenseapi.pojo.Preference;

public interface PreferenceService extends GenericService<Preference, Long> {
    Preference getUserPreferences();
    Preference updateUserPreferences(PreferenceUpdateDTO preferenceUpdateDTO);
}
