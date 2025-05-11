package com.alerts.factories;
import com.alerts.alert_types.*;
public class ECGAlertFactory implements AlertFactory{
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp){
        Alert alert = new ECGAlert(patientId, condition, timestamp);
        return alert;
    }
    
}
