package com.sapient.wellington.oneviewassignment.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.wellington.oneviewassignment.constant.OneViewConstant;
import com.sapient.wellington.oneviewassignment.model.Currency;
import com.sapient.wellington.oneviewassignment.model.InputRecord;
import com.sapient.wellington.oneviewassignment.model.OutputRecord;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OneViewHelper {

    public static List<OutputRecord> generateOutputData(String countryOrCity, List<InputRecord> inputRecords, List<Currency> currencies){
        OutputRecord getMaleRecord = generateOutputRecordForGender(inputRecords, currencies, OneViewConstant.M, countryOrCity);
        OutputRecord getFemaleRecord = generateOutputRecordForGender(inputRecords, currencies, OneViewConstant.F, countryOrCity);

        return Arrays.asList(getMaleRecord, getFemaleRecord);
    }

    public static OutputRecord generateOutputRecordForGender(List<InputRecord> records, List<Currency> currencies, String gender, String cityOrCountry) {
        List<InputRecord> inputRecords = records.parallelStream()
                .filter(inputRecord -> inputRecord.getGender().equals(gender)).collect(Collectors.toList());
        Currency currency = currencies
                .parallelStream()
                .filter(cur -> cur.getName().equals(records.get(OneViewConstant.ZERO).getCurrency()))
                .findAny()
                .get();

        Double averageInUSD = getAverageValueInDollar(inputRecords, currency);

        return new OutputRecord(
                cityOrCountry,
                gender,
                averageInUSD
        );
    }

    public static List<OutputRecord> convertInputRecordsToOutputRecords(List<InputRecord> inputRecords) throws IOException {
        List<InputRecord> inputRecordsWithCountry = getInputRecordsWithCountry(inputRecords);
        List<InputRecord> inputRecordsWithoutCountry = getInputRecordsWithoutCountry(inputRecords);

        Map<String, List<InputRecord>> recordsGroupByCountryOrCity = inputRecordsWithCountry.parallelStream().collect(Collectors.groupingBy(InputRecord::getCountry));
        recordsGroupByCountryOrCity.putAll(inputRecordsWithoutCountry.parallelStream().collect(Collectors.groupingBy(InputRecord::getCity)));

        Currency[] currencies = new ObjectMapper().readValue(new File(OneViewConstant.CURRENCY_FILE_PATH), Currency[].class);
        List<Currency> currencyList = Arrays.asList(currencies);

        return recordsGroupByCountryOrCity.entrySet()
                .parallelStream()
                .map(es -> OneViewHelper.generateOutputData(es.getKey(),es.getValue(), currencyList))
                .flatMap(or -> or.stream()).collect(Collectors.toList());
    }

    public static Double getAverageValueInDollar(List<InputRecord> records, Currency currency) {
        return records.parallelStream()
                      .mapToDouble(InputRecord::getAverageIncome)
                      .average()
                      .getAsDouble() / currency.getRate();
    }

    public static List<InputRecord> getInputRecordsWithCountry(List<InputRecord> inputRecords) {
        return inputRecords.parallelStream().filter(inputRecord -> !inputRecord.getCountry().equals("")).collect(Collectors.toList());
    }

    public static List<InputRecord> getInputRecordsWithoutCountry(List<InputRecord> inputRecords) {
        return inputRecords.parallelStream().filter(inputRecord -> inputRecord.getCountry().equals("")).collect(Collectors.toList());
    }
}
