package com.sapient.wellington.oneviewassignment.service;

import com.sapient.wellington.oneviewassignment.model.InputRecord;
import com.sapient.wellington.oneviewassignment.model.OutputRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface OneViewService {
    public void processAverageIncome() throws IOException;
    public List<OutputRecord> convertInputRecordsToOutputRecords(List<InputRecord> inputRecords) throws IOException;
}
