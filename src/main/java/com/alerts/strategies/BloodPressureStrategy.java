package com.alerts.strategies;

import java.util.ArrayList;
import java.util.List;

import com.alerts.AlertGenerator;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy{
    static final double SYSTOLIC_PRESSURE_MAX = 180;
    static final double SYSTOLIC_PRESSURE_MIN = 90;
    static final double DIASTOLIC_PRESSURE_MAX = 120;
    static final double DIASTOLIC_PRESSURE_MIN = 60;
    /**
     * Checks the blood pressure levels of a patient and triggers alerts
     * @param patient         The patient whose records are to be checked
     * @param alertGenerator The alert generator to trigger alerts
     */
    @Override
    public void checkAlert(Patient patient, AlertGenerator alertGenerator){
        List<PatientRecord> systolics = new ArrayList<>();
        List<PatientRecord> diastolics = new ArrayList<>();
        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())){
            if (record.getRecordType().equalsIgnoreCase("SystolicPressure")){
                systolics.add(record);
            } else if (record.getRecordType().equalsIgnoreCase("DiastolicPressure")){
                diastolics.add(record);
            }
        }
        for (PatientRecord record : systolics){
            if (record.getMeasurementValue() > SYSTOLIC_PRESSURE_MAX || record.getMeasurementValue() < SYSTOLIC_PRESSURE_MIN){
                alertGenerator.triggerAlert(new BloodPressureAlertFactory().createAlert(String.valueOf(record.getPatientId()), "Critical systolic-pressure of "+record.getMeasurementValue()+ " outside threshold", record.getTimestamp()));
            }
        }
        for (PatientRecord record : diastolics){
            if (record.getMeasurementValue() > DIASTOLIC_PRESSURE_MAX || record.getMeasurementValue() < DIASTOLIC_PRESSURE_MIN){
                alertGenerator.triggerAlert(new BloodPressureAlertFactory().createAlert(String.valueOf(record.getPatientId()), "Critical diastolic-pressure of " +record.getMeasurementValue()+ " outside threshold", record.getTimestamp()));
            }
        }
        if (systolics.size() >= 3){
            for (int i = 2; i < systolics.size(); i++) {
                double a = systolics.get(i-2).getMeasurementValue();
                double b = systolics.get(i-1).getMeasurementValue();
                double c = systolics.get(i).getMeasurementValue();
                if (Math.abs(b-a) > 10 && Math.abs(c-b) > 10){
                    alertGenerator.triggerAlert(new BloodPressureAlertFactory().createAlert(String.valueOf(systolics.get(i).getPatientId()), "Critical systolic-pressure trend changes exceeding 10 mmHg", systolics.get(i).getTimestamp()));
                }
            }
        }
        if (diastolics.size() >= 3){
            for (int i = 2; i < diastolics.size(); i++) {
                double a = diastolics.get(i-2).getMeasurementValue();
                double b = diastolics.get(i-1).getMeasurementValue();
                double c = diastolics.get(i).getMeasurementValue();
                if (Math.abs(b-a) > 10 && Math.abs(c-b) > 10){
                    alertGenerator.triggerAlert(new BloodPressureAlertFactory().createAlert(String.valueOf(diastolics.get(i).getPatientId()), "Critical diastolic-pressure trend changes exceeding 10 mmHg", diastolics.get(i).getTimestamp()));
                }
            }
        }
    }
     
}
