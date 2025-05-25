package com.alerts.strategies;

import java.util.ArrayList;
import java.util.List;

import com.alerts.AlertGenerator;
import com.alerts.factories.HypoHypoxAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HypoHypoxStrategy implements AlertStrategy{
    static final double SYSTOLIC_PRESSURE_MIN = 90;
    static final double BLOOD_OXYGEN_SATURATION_MIN = 92;

    /**
     * Checks the blood pressure and blood oxygen saturation levels of a patient
     * and triggers alerts if both are below critical thresholds.
     *
     * @param patient         The patient whose records are to be checked
     * @param alertGenerator The alert generator to trigger alerts
     */
    @Override
    public void checkAlert(Patient patient, AlertGenerator alertGenerator){
        List<PatientRecord> systolics = new ArrayList<>();
        List<PatientRecord> saturations = new ArrayList<>();

        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())){
            if (record.getRecordType().equalsIgnoreCase("SystolicPressure")){
                systolics.add(record);
            }
            else if (record.getRecordType().equalsIgnoreCase("Saturation")){
                saturations.add(record);
            }
        }
        long currentTimeSys = 0;
        long currentTimeSat = 0;
        int patientId = -1;
        boolean flagSystolic = false;
        boolean flagSaturation = false;
        for (PatientRecord record : systolics){
            if (record.getMeasurementValue() < SYSTOLIC_PRESSURE_MIN){
                flagSystolic = true;
                patientId = record.getPatientId();
                currentTimeSys = record.getTimestamp();
            }
        }
        for (PatientRecord record : saturations){
            if (record.getMeasurementValue() < BLOOD_OXYGEN_SATURATION_MIN){
                flagSaturation = true;
                patientId = record.getPatientId();
                currentTimeSat = record.getTimestamp();
            }
        }
        if (flagSaturation && flagSystolic && currentTimeSys !=0 && currentTimeSat != 0){
            alertGenerator.triggerAlert( new HypoHypoxAlertFactory().createAlert(String.valueOf(patientId), "Hypotensive Hypoxemia Alert", Math.max(currentTimeSat, currentTimeSys)));
        }
    }
}