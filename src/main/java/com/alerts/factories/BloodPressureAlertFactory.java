package com.alerts.factories;
import com.alerts.alert_types.Alert;
import com.alerts.alert_types.BloodPressureAlert;

public class BloodPressureAlertFactory implements AlertFactory{

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp){
        Alert alert = new BloodPressureAlert(patientId, condition, timestamp);
        return alert;
    }
}