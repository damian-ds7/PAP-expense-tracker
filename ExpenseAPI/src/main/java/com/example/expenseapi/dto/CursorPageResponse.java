package com.example.expenseapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageResponse<T>{
    private T data;
    private Long nextLastId;
    private String nextLastKey;
    private boolean hasMore;
}
