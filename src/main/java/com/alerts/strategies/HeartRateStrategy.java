package com.alerts.strategies;

import java.util.ArrayList;
import java.util.List;

import com.alerts.AlertGenerator;
import com.alerts.factories.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HeartRateStrategy implements AlertStrategy{
    

    /**
     * Checks the ECG readings of a patient and triggers alerts
     * @param patient         The patient whose records are to be checked
     * @param alertGenerator The alert generator to trigger alerts
     */
    @Override
    public void checkAlert(Patient patient, AlertGenerator alertGenerator){
        List<PatientRecord> records = new ArrayList<>();
        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())){
            if (record.getRecordType().equalsIgnoreCase("ECG")){
                records.add(record);
            }
        }
        //calculate mean
        double total = 0;
        int n = 0;
        for (PatientRecord record : records){
            if (record.getRecordType().equalsIgnoreCase("ECG")){
                total += record.getMeasurementValue();
                n++;
            }
        }
        if (n == 0){return;} //no ECG data
        double avg = total / n;
        //calculate standard deviation
        double sumSquaredDiff = 0;
        for (PatientRecord record: records){
            if (record.getRecordType().equalsIgnoreCase("ECG")){
                double diff = record.getMeasurementValue() - avg;
                sumSquaredDiff += diff * diff;
            }
        }
        double stdDev = Math.sqrt(sumSquaredDiff / n);
        //check which data is 2SDs away from mean to trigger alert
        for (PatientRecord record : records) {
            if (record.getRecordType().equalsIgnoreCase("ECG")) {
                if (Math.abs(record.getMeasurementValue() - avg) > 2 * stdDev && record.getMeasurementValue() > avg) {
                    alertGenerator.triggerAlert( new ECGAlertFactory().createAlert(String.valueOf(record.getPatientId()),"Abnormal ECG reading detected", record.getTimestamp()));
                }
            }
        }
    }
     
}
