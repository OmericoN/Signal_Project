package com.alerts.decorators;
import com.alerts.alert_types.Alert;
public class AlertDecorator implements Alert{
    protected Alert decoratedAlert;
    public AlertDecorator(Alert alert){
        this.decoratedAlert = alert;
    }
    @Override
    public String getPatientId(){
        return decoratedAlert.getPatientId();
    }

    @Override
    public String getCondition(){
        return decoratedAlert.getCondition();
    }

    @Override
    public long getTimestamp(){
        return decoratedAlert.getTimestamp();
    }
    
}
