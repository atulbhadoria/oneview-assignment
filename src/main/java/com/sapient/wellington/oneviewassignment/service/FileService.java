package com.sapient.wellington.oneviewassignment.service;

import com.sapient.wellington.oneviewassignment.model.InputRecord;
import com.sapient.wellington.oneviewassignment.model.OutputRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface FileService {
    public List<InputRecord> readRecords(String filePath) throws IOException;
    public void witeRecords(String filePath, List<OutputRecord> outputRecords);
}
