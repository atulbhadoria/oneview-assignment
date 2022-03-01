package com.sapient.wellington.oneviewassignment.helper;

import com.sapient.wellington.oneviewassignment.model.InputRecord;
import com.sapient.wellington.oneviewassignment.model.OutputRecord;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;

public class CSVHeper {
    public static InputRecord parseCSVToInputRecord(CSVRecord csvRecord) {
        return new InputRecord(
                csvRecord.get(0),
                csvRecord.get(1),
                csvRecord.get(2),
                csvRecord.get(3),
                Double.valueOf(csvRecord.get(4))
        );
    }

    public static void printRecordInCSV(CSVPrinter csvPrinter, OutputRecord outputRecord){
        try {
            csvPrinter.printRecord(outputRecord.getCityOrCountry(),outputRecord.getGender(),outputRecord.getAverageIncome());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
