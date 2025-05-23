package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy {
  //we used camel case due to the java variable naming conventions
    private String BaseDirectory; 
  // upper snake case since it is constant

    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    /**
     * constructor with the baseDirectory parameter
     * @param baseDirectory the chosen directory
     */

    public FileOutputStrategy(String baseDirectory) {
        //changed name w the uppercase
        this.BaseDirectory = baseDirectory;
    }
    /**
     * Function that outputs the patient record
     * @param patientId which corresponds to the patient ID
     * @param timestamp corresponds to the time stamp
     * @param label which corresponds to the data label
     * @param data corresponds to the inputted data
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            //changed name
            Files.createDirectories(Paths.get(BaseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        //changed name
        String FilePath = file_map.computeIfAbsent(label, k -> Paths.get(BaseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}