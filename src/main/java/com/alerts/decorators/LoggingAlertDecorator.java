package com.alerts.decorators;

import com.alerts.alert_types.Alert;
// Useful to track alerts for debugging
public class LoggingAlertDecorator extends AlertDecorator {

    public LoggingAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public String getCondition() {
        logAlert();
        return decoratedAlert.getCondition();
    }

    private void logAlert() {
        System.out.println("Logging Alert: " + decoratedAlert.getCondition() +" for Patient ID: " + decoratedAlert.getPatientId() +" at Timestamp: " + decoratedAlert.getTimestamp());
    }
}