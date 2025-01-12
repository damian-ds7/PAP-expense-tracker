package com.example.expenseapi.service;

import com.example.expenseapi.dto.PreferenceDTO;
import com.example.expenseapi.exception.BadRequestException;
import com.example.expenseapi.mapper.PreferenceMapper;
import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.pojo.MethodOfPayment;
import com.example.expenseapi.pojo.Preference;
import com.example.expenseapi.repository.CurrencyRepository;
import com.example.expenseapi.repository.MethodOfPaymentRepository;
import com.example.expenseapi.repository.PreferenceRepository;
import com.example.expenseapi.utils.AuthHelper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class PreferenceServiceImpl extends GenericServiceImpl<Preference, Long> implements PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final CurrencyRepository currencyRepository;
    private final MethodOfPaymentRepository methodOfPaymentRepository;
    private final PreferenceMapper preferenceMapper;

    public PreferenceServiceImpl(PreferenceRepository repository, CurrencyRepository currencyRepository, MethodOfPaymentRepository methodOfPaymentRepository, PreferenceMapper preferenceMapper) {
        super(repository);
        this.preferenceRepository = repository;
        this.currencyRepository = currencyRepository;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
        this.preferenceMapper = preferenceMapper;
    }

    @Override
    public PreferenceDTO getUserPreferences() {
        return preferenceMapper.preferenceToPreferenceDTO(preferenceRepository.getPreferenceById(AuthHelper.getUser().getId()));
    }

    @Override
    @CacheEvict(value = {
            "expInfoGroup",
            "expInfoAllGroups"
    }, allEntries = true)
    public PreferenceDTO updateUserPreferences(PreferenceDTO preferenceDTO) {
        Preference pref = preferenceRepository.getPreferenceById(AuthHelper.getUser().getId());
        if (preferenceDTO.getCurrencySymbol() != null) {
            Currency currency = currencyRepository.findBySymbol(preferenceDTO.getCurrencySymbol())
                    .orElseThrow(() -> new BadRequestException("Currency not found " + preferenceDTO.getCurrencySymbol()));
            pref.setCurrency(currency);
        }
        if (preferenceDTO.getMethodOfPayment() != null) {
            MethodOfPayment method = methodOfPaymentRepository.findByName(preferenceDTO.getMethodOfPayment())
                    .orElseThrow(() -> new BadRequestException(("Method of payment not found ")));
            pref.setMethod(method);
        }
        if (preferenceDTO.getLanguage() != null)
            pref.setLanguage(preferenceDTO.getLanguage());
        return preferenceMapper.preferenceToPreferenceDTO(preferenceRepository.save(pref));
    }
}
