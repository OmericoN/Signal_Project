package com.cardio_generator.generators;

import java.util.Random;
import java.util.logging.Logger;

import com.cardio_generator.outputs.OutputStrategy; 

public class AlertGenerator implements PatientDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(AlertGenerator.class.getName()); // 5.2.4 Constant field names must have UPPER_SNAME_CASE hence changed names must be nouns hence changed
    public static final Random RANDOM_GENERATOR = new Random(); // 5.2.4 Constant field names must have UPPER_SNAME_CASE hence changed
    private boolean[] alertStates; // false = resolved, true = pressed
    //5.2.5 Non Constant filed name: must be lowerCamelCase hence changed

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                //5.2.7 local variable name must be lowerCamelCase hence changed 
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            LOGGER.severe("An error occurred while generating alert data for patient " + patientId); //6.2 caught excepetions: using logging is the better practice hence changed
        }
    }
}
