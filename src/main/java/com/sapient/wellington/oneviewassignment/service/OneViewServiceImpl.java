package com.sapient.wellington.oneviewassignment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.wellington.oneviewassignment.constant.OneViewConstant;
import com.sapient.wellington.oneviewassignment.helper.OneViewHelper;
import com.sapient.wellington.oneviewassignment.model.Currency;
import com.sapient.wellington.oneviewassignment.model.InputRecord;
import com.sapient.wellington.oneviewassignment.model.OutputRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OneViewServiceImpl implements OneViewService {

    @Autowired
    private FileService fileService;

    @Override
    public void processAverageIncome() throws IOException {
        List<InputRecord> inputRecords = fileService.readRecords(OneViewConstant.INPUT_FILE_PATH);

        List<OutputRecord> outputRecords = convertInputRecordsToOutputRecords(inputRecords);

        fileService.witeRecords(OneViewConstant.OUTPUT_FILE_PATH, outputRecords);
    }

    @Override
    public List<OutputRecord> convertInputRecordsToOutputRecords(List<InputRecord> inputRecords) throws IOException {
        List<InputRecord> inputRecordsWithCountry = OneViewHelper.getInputRecordsWithCountry(inputRecords);
        List<InputRecord> inputRecordsWithoutCountry = OneViewHelper.getInputRecordsWithoutCountry(inputRecords);

        Map<String, List<InputRecord>> recordsGroupByCountryOrCity = inputRecordsWithCountry.parallelStream().collect(Collectors.groupingBy(InputRecord::getCountry));
        recordsGroupByCountryOrCity.putAll(inputRecordsWithoutCountry.parallelStream().collect(Collectors.groupingBy(InputRecord::getCity)));

        Currency[] currencies = new ObjectMapper().readValue(new File(OneViewConstant.CURRENCY_FILE_PATH), Currency[].class);
        List<Currency> currencyList = Arrays.asList(currencies);

        return recordsGroupByCountryOrCity.entrySet()
                .parallelStream()
                .map(es -> OneViewHelper.generateOutputData(es.getKey(),es.getValue(), currencyList))
                .flatMap(or -> or.stream()).collect(Collectors.toList());
    }
}
