package com.sapient.wellington.oneviewassignment.service;

import com.sapient.wellington.oneviewassignment.constant.OneViewConstant;
import com.sapient.wellington.oneviewassignment.helper.CSVHeper;
import com.sapient.wellington.oneviewassignment.model.InputRecord;
import com.sapient.wellington.oneviewassignment.model.OutputRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVFileServiceImpl implements FileService{
    Logger logger = LoggerFactory.getLogger(CSVFileServiceImpl.class);

    @Override
    public List<InputRecord> readRecords(String filePath) throws IOException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(filePath));
                CSVParser csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(reader);

        ) {
            List<InputRecord> inputRecords = csvParser.getRecords()
                    .parallelStream()
                    .map(CSVHeper::parseCSVToInputRecord)
                    .collect(Collectors.toList());

            logger.info(OneViewConstant.DATA_READ_SUCCESS_MSG);
            logger.info(OneViewConstant.FILE_NAME_TEXT+filePath);
            return inputRecords;
        }
        catch (IOException ioException){
            logger.error(ioException.getLocalizedMessage());
            throw ioException;
        }


    }

    @Override
    public void witeRecords(String filePath, List<OutputRecord> outputRecords) {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader(OneViewConstant.CITY_OR_COUNTRY_HEADER, OneViewConstant.GENDER_HEADER, OneViewConstant.AVERAGE_INCOME_USD_HEADER));
        ) {
            outputRecords.parallelStream()
                    .sorted(Comparator.comparing(OutputRecord::getCityOrCountry)
                            .thenComparing(OutputRecord::getGender)
                            .thenComparing(OutputRecord::getAverageIncome))
                    .forEachOrdered(outputRecord -> CSVHeper.printRecordInCSV(csvPrinter,outputRecord));

            logger.info(OneViewConstant.DATA_WRITE_SUCCESS_MSG);
            logger.info(OneViewConstant.FILE_NAME_TEXT+filePath);
        }
        catch (IOException ioException){
            logger.error(ioException.getLocalizedMessage());
        }
    }
}
