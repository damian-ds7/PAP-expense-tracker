package com.example.expenseapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceDTO {
    private String currencySymbol;
    private String methodOfPayment;
    private String language;
}
