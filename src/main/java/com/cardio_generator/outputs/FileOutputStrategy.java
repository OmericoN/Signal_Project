package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FileOutputStrategy is an implementation of OutputStrategy that writes patient data
 * to text files. Each label (e.g., "ECG", "Alert") is written to a separate file
 * within the specified base directory. Data is appended to the corresponding file.
 */
public class FileOutputStrategy implements OutputStrategy { //5.2.2 Class Name: class names are written in UpperCamlelCase hence changed

    private String baseDirectory; //5.2.5 Non constant field name: should be written in lowerCamelCase hence changed

    public static final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>(); //5.2.4 Constant Name: constant names are written in UPPER_SNAKE_CASE and must be final and static hence changed

    /**
     * Constructs a FileOutputStrategy with the specified base directory.
     *
     * @param baseDirectory the directory where output files will be stored
     */
    public FileOutputStrategy(String baseDirectory) {//Constructor must always match the correct class name declaration hence changed

        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs a single data record for a patient by appending it to the appropriate file.
     * Each label is written to its own file within the base directory.
     *
     * @param patientId the unique identifier of the patient
     * @param timestamp the time at which the data was generated, in milliseconds since epoch
     * @param label     a label describing the type of data (e.g., "ECG", "Alert")
     * @param data      the actual data value or message to output
     */
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