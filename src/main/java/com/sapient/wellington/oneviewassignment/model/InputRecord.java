package com.sapient.wellington.oneviewassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputRecord {
    private String city;
    private String country;
    private String gender;
    private String currency;
    private Double averageIncome;
}
