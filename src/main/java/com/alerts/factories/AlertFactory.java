package com.alerts.factories;
import com.alerts.alert_types.Alert;
public interface AlertFactory {
    public Alert createAlert(String patientId, String condition, long timestamp);
    
}
