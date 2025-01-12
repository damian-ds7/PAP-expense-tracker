package com.example.expenseapi.service;

import com.example.expenseapi.dto.PreferenceUpdateDTO;
import com.example.expenseapi.exception.BadRequestException;
import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.pojo.MethodOfPayment;
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

    public PreferenceServiceImpl(PreferenceRepository repository, CurrencyRepository currencyRepository, MethodOfPaymentRepository methodOfPaymentRepository) {
        super(repository);
        this.preferenceRepository = repository;
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
        Currency currency = currencyRepository.findBySymbol(preferenceUpdateDTO.getCurrencySymbol())
                .orElseThrow(() -> new BadRequestException("Currency not found " + preferenceUpdateDTO.getCurrencySymbol()));
        pref.setCurrency(currency);
        MethodOfPayment method = methodOfPaymentRepository.findByName(preferenceUpdateDTO.getMethodOfPayment())
                .orElseThrow(() -> new BadRequestException(("Method of payment not found ")));
        pref.setMethod(method);
        if (preferenceUpdateDTO.getLanguage() != null)
            pref.setLanguage(preferenceUpdateDTO.getLanguage());
        return preferenceRepository.save(pref);
    }
}
