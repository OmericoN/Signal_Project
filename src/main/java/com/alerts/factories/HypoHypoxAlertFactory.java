package com.alerts.factories;
import com.alerts.alert_types.Alert;
import com.alerts.alert_types.HypoHypoxAlert;
//Special Hypotensive Hypoxemia Condition Alert Factory
public class HypoHypoxAlertFactory implements AlertFactory{
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp){
        Alert alert = new HypoHypoxAlert(patientId, condition, timestamp);
        return alert;
    }
}