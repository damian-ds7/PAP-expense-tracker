package com.example.expenseapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceUpdateDTO {
    private String currencySymbol;
    private String methodOfPayment;
}
