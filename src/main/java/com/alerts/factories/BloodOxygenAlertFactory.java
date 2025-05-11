package com.alerts.factories;
import com.alerts.alert_types.Alert;
import com.alerts.alert_types.BloodOxygenAlert;

public class BloodOxygenAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp){
        Alert alert = new BloodOxygenAlert(patientId, condition, timestamp);
        return alert;
    }
}
