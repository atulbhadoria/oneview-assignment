package com.sapient.wellington.oneviewassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputRecord {
    private String cityOrCountry;
    private String gender;
    private Double averageIncome;
}
