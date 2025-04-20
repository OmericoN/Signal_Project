package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy { //5.2.2 Class Name: class names are written in UpperCamlelCase hence changed

    private String baseDirectory; //5.2.5 Non constant field name: should be written in lowerCamelCase hence changed

    private static final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>(); //5.2.4 Constant Name: constant names are written in UPPER_SNAKE_CASE and must be final and static hence changed

    public FileOutputStrategy(String baseDirectory) {//Constructor must always match the correct class name declaration hence changed

        this.baseDirectory = baseDirectory;
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String filePath = FILE_MAP.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString()); //5.2.3 Variable Name: variable names are written in lowerCamelCase hence changed

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}