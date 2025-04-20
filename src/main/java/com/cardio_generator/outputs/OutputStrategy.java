package com.cardio_generator.outputs;

/**
 * OutputStrategy defines a contract for outputting patient data.
 * Implementations of this interface determine how and where the generated
 * patient data is sent or stored (e.g., console, file, network).
 */
public interface OutputStrategy {
    /**
     * Outputs a single data record for a patient.
     *
     * @param patientId the unique identifier of the patient
     * @param timestamp the time at which the data was generated, in milliseconds since epoch
     * @param label     a label describing the type of data (e.g., "ECG", "Alert")
     * @param data      the actual data value or message to output
     */
    void output(int patientId, long timestamp, String label, String data);
}
