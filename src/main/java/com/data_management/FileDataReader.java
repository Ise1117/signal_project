package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * This reads the data from files in a specified directory and stores it in data storage.
 * It implements the {@link DataReader} interface to provide file-based reading function
*/

public class FileDataReader implements DataReader {

    private final String outputDirectory;
    /**
     * it constructs a filedatareader with the specified output directory.
     * @param outputDIrectory is the directory where the files are located
     * 
    */

    public FileDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        File directory = new File(outputDirectory);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("The specified output directory does not exist or is not a directory.");
        }
        DataStorage storage = dataStorage;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    parseFile(file, storage);
                }
            }
        }
    }
    /**
     * It parses a file and stores the data iun data storage
     * @param file is the file to be parsed
     * @param dataStorage is where we store the data
     */
     private void parseFile(File file, DataStorage dataStorage) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        int patientId = Integer.parseInt(parts[0]);
                        long timestamp = Long.parseLong(parts[1]);
                        String recordType = parts[2];
                        double measurementValue = Double.parseDouble(parts[3]);

                        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid line in file " + file.getName() + ": " + line);
                    }
                } else {
                    System.err.println("Malformed line in file " + file.getName() + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + file.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        // Not needed for file-based data reader
    }

    @Override
    public void stop() {
        // Not needed for file-based data reader
    }
}