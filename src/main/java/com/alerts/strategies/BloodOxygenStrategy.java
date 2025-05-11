package com.alerts.strategies;
import java.util.ArrayList;
import java.util.List;

import com.alerts.AlertGenerator;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodOxygenStrategy implements AlertStrategy {
    static final double BLOOD_OXYGEN_SATURATION_MIN = 92;
    static final double RAPID_DROP_THRESHOLD = 5;
    @Override
    public void checkAlert(Patient patient, AlertGenerator alertGenerator){
        List<PatientRecord> saturations = new ArrayList<>();
        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())){
            if (record.getRecordType().equalsIgnoreCase("Saturation")){
                saturations.add(record);
            }
        }

        for (int i = 0; i < saturations.size(); i++){
            PatientRecord record = saturations.get(i);
            if (record.getMeasurementValue() < BLOOD_OXYGEN_SATURATION_MIN){
                alertGenerator.triggerAlert( new BloodOxygenAlertFactory().createAlert(String.valueOf(record.getPatientId()), "Blood saturation dropped below 92%", record.getTimestamp()));
            }
            if (i > 0){
                PatientRecord prevRecord = saturations.get(i-1);
                if (prevRecord.getMeasurementValue() - record.getMeasurementValue() >= RAPID_DROP_THRESHOLD && (record.getTimestamp() - prevRecord.getTimestamp()) <= 10 * 60 * 1000){
                    alertGenerator.triggerAlert( new BloodOxygenAlertFactory().createAlert(String.valueOf(record.getPatientId()), "Rapid blood saturation decline in the past 10 mins", record.getTimestamp()));
                }
            }
        }
    }
}
