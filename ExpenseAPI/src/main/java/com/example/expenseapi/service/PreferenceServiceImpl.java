package com.example.expenseapi.service;

import com.example.expenseapi.dto.PreferenceUpdateDTO;
import com.example.expenseapi.pojo.Preference;
import com.example.expenseapi.repository.CurrencyRepository;
import com.example.expenseapi.repository.MethodOfPaymentRepository;
import com.example.expenseapi.repository.PreferenceRepository;
import com.example.expenseapi.utils.AuthHelper;
import org.springframework.stereotype.Service;

@Service
public class PreferenceServiceImpl extends GenericServiceImpl<Preference, Long> implements PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final CurrencyRepository currencyRepository;
    private final MethodOfPaymentRepository methodOfPaymentRepository;

    public PreferenceServiceImpl(PreferenceRepository repository, PreferenceRepository preferenceRepository, CurrencyRepository currencyRepository, MethodOfPaymentRepository methodOfPaymentRepository) {
        super(repository);
        this.preferenceRepository = preferenceRepository;
        this.currencyRepository = currencyRepository;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
    }

    @Override
    public Preference getUserPreferences() {
        return preferenceRepository.getPreferenceById(AuthHelper.getUser().getId());
    }

    @Override
    public Preference updateUserPreferences(PreferenceUpdateDTO preferenceUpdateDTO) {
        Preference pref = getUserPreferences();
        pref.setCurrency(currencyRepository.findBySymbol(preferenceUpdateDTO.getCurrencySymbol()));
        pref.setMethod(methodOfPaymentRepository.findByName(preferenceUpdateDTO.getMethodOfPayment()));
        return preferenceRepository.save(pref);
    }
}
