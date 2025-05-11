package com.alerts;
import java.util.List;

import com.alerts.strategies.AlertStrategy;
import com.alerts.strategies.BloodOxygenStrategy;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.HypoHypoxStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    static final double SYSTOLIC_PRESSURE_MAX = 180;
    static final double SYSTOLIC_PRESSURE_MIN = 90;
    static final double DIASTOLIC_PRESSURE_MAX = 120;
    static final double DIASTOLIC_PRESSURE_MIN = 60;
    static final double BLOOD_OXYGEN_SATURATION_MIN = 92;
    static final double RAPID_DROP_THRESHOLD = 5;
    static final long TEN_MINUTES_MS = 10 * 60 * 1000;
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Implementation goes here
        if (patient == null){
            System.out.println("Invalid patient");
        } else{
            AlertStrategy bloodOxygen = new BloodOxygenStrategy();
            AlertStrategy bloodPressure = new BloodPressureStrategy();
            AlertStrategy heartRate = new HeartRateStrategy();
            AlertStrategy hypoHypox = new HypoHypoxStrategy();
            bloodOxygen.checkAlert(patient, this);
            bloodPressure.checkAlert(patient, this);
            heartRate.checkAlert(patient, this);
            hypoHypox.checkAlert(patient, this);

        }

    }

    /**
     * Extends functionality of the evaluateData() method
     * Analyzes ECG data to detect abnormal readings
     * Does it by checking which data lies 2 Standard Deviations from the mean and is above the mean average
     * Then triggers alert
     * @param records a list of PatientRecord objects containing ECG data
     */
    private void checkEcgData(List<PatientRecord> records){
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
                    com.alerts.Alert alert = new com.alerts.Alert(String.valueOf(record.getPatientId()),"Abnormal ECG reading detected", record.getTimestamp());
                    //triggerAlert(alert);
                }
            }
        }
    }

    /**
     * Monitors blood pressure readings by distincly evaluating systolic and diastolic pressures
     * if systolic or diastolic pressure is outside their threshold -> triggers alert
     * if systolic or diastolic pressure rapidly changes consecutively with steps exceeding 10 mmHh -> triggers alert
     * @param systolics list of PatientRecord objects with systolic pressure readings
     * @param diastolics list of PatientRecord objects with diastolic pressure readings
     */
    private void checkBloodPressure(List<PatientRecord> systolics, List<PatientRecord> diastolics){
        
        for (PatientRecord record : systolics){
            if (record.getMeasurementValue() > SYSTOLIC_PRESSURE_MAX || record.getMeasurementValue() < SYSTOLIC_PRESSURE_MIN){
               // Alert alert = new Alert(String.valueOf(record.getPatientId()), "Critical systolic-pressure of "+record.getMeasurementValue()+ " outside threshold", record.getTimestamp());
                //triggerAlert(alert);
            }
        }
        for (PatientRecord record : diastolics){
            if (record.getMeasurementValue() > DIASTOLIC_PRESSURE_MAX || record.getMeasurementValue() < DIASTOLIC_PRESSURE_MIN){
               // Alert alert = new Alert(String.valueOf(record.getPatientId()), "Critical diastolic-pressure of " +record.getMeasurementValue()+ " outside threshold", record.getTimestamp());
                //triggerAlert(alert);
            }
        }
        if (systolics.size() >= 3){
            for (int i = 2; i < systolics.size(); i++) {
                double a = systolics.get(i-2).getMeasurementValue();
                double b = systolics.get(i-1).getMeasurementValue();
                double c = systolics.get(i).getMeasurementValue();
                if (Math.abs(b-a) > 10 && Math.abs(c-b) > 10){
                    //Alert alert = new Alert(String.valueOf(systolics.get(i).getPatientId()), "Critical systolic-pressure trend changes exceeding 10 mmHg", systolics.get(i).getTimestamp());
                    //triggerAlert(alert);
                }
            }
        }
        if (diastolics.size() >= 3){
            for (int i = 2; i < diastolics.size(); i++) {
                double a = diastolics.get(i-2).getMeasurementValue();
                double b = diastolics.get(i-1).getMeasurementValue();
                double c = diastolics.get(i).getMeasurementValue();
                if (Math.abs(b-a) > 10 && Math.abs(c-b) > 10){
                   // Alert alert = new Alert(String.valueOf(diastolics.get(i).getPatientId()), "Critical diastolic-pressure trend changes exceeding 10 mmHg", diastolics.get(i).getTimestamp());
                    //triggerAlert(alert);
                }
            }
        }
    }

    /**
     * Monitors blood oxygen saturation readings to detect both rapid changes and critical reading outside threshold
     * if saturation is below 92% -> trigger alert
     * if saturation readings change 5% within 10 mins timeframe -> triggers alert
     * @param saturations list of PatientRecord objects containing saturation readings
     */
    private void checkBloodSaturation(List<PatientRecord> saturations){
        for (int i = 0; i < saturations.size(); i++){
            PatientRecord record = saturations.get(i);
            if (record.getMeasurementValue() < BLOOD_OXYGEN_SATURATION_MIN){
               // Alert alert = new Alert(String.valueOf(record.getPatientId()), "Blood saturation dropped below 92%", record.getTimestamp());
               // triggerAlert(alert);
            }
            if (i > 0){
                PatientRecord prevRecord = saturations.get(i-1);
                if (prevRecord.getMeasurementValue() - record.getMeasurementValue() >= 5 && (record.getTimestamp() - prevRecord.getTimestamp()) <= 10 * 60 * 1000){
                    //Alert alert = new Alert(String.valueOf(record.getPatientId()), "Rapid blood saturation decline in the past 10 mins", record.getTimestamp());
                    //triggerAlert(alert);
                }
            }
        }
    }

    /**
     * Identifies dangerous condition comprising combination of low systolic pressure and low blood saturation
     * if both blood saturation and systolic pressure are below their threshold -> triggers alert
     * @param systolics list of PatientRecord objects of systolic pressure readings
     * @param saturations list of PatientRecord objects of saturation readings
     */
    private void checkHypotHypox(List<PatientRecord> systolics,List<PatientRecord> saturations){
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
            //Alert alert = new Alert(String.valueOf(patientId), "Hypotensive Hypoxemia Alert", Math.max(currentTimeSat, currentTimeSys));
            //triggerAlert(alert);
        }

    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    public void triggerAlert(com.alerts.alert_types.Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        System.out.println("!---------------An Alert has been Triggered-----------------!");
        System.out.println("Patient ID: " + alert.getPatientId());
        System.out.println("Patient's Condition: "+ alert.getCondition());
        System.out.println("Timestamp: " + alert.getTimestamp());
        System.out.println("!---------------End of Alert-----------------------!");
    }  
}
