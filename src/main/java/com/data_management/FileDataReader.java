package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class FileDataReader implements DataReader {

    private String filePath;
    public FileDataReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                int patientId = Integer.parseInt(tokenizer.nextToken().trim());
                double measurementValue = Double.parseDouble(tokenizer.nextToken().trim());
                String recordType = tokenizer.nextToken().trim();
                long timestamp = Long.parseLong(tokenizer.nextToken().trim());

                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
        }
    }
}
