package com.example.expenseapi.mapper;

import com.example.expenseapi.dto.PreferenceDTO;
import com.example.expenseapi.pojo.Preference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PreferenceMapper {
    @Mapping(source = "currency.symbol", target = "currencySymbol")
    @Mapping(source = "language", target = "language")
    @Mapping(source = "method.name", target = "methodOfPayment")
    PreferenceDTO preferenceToPreferenceDTO(Preference preference);
}
