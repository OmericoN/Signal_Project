package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;


/**
 * BloodSaturationDataGenerator simulates blood oxygen saturation (SpO2) values for patients.
 * It generates realistic saturation values with small fluctuations and outputs them
 * using the provided OutputStrategy.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;


    /**
     * Constructs a BloodSaturationDataGenerator for the specified number of patients.
     * Initializes each patient's saturation value to a baseline between 95 and 100.
     *
     * @param patientCount the number of patients to simulate
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }


    /**
     * Generates and outputs a new blood saturation value for the specified patient.
     * The value fluctuates slightly from the previous value and is kept within a realistic range.
     *
     * @param patientId      the unique identifier of the patient
     * @param outputStrategy the strategy to use for outputting the data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
