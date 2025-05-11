package com.alerts.decorators;

import com.alerts.AlertGenerator;
import com.alerts.alert_types.Alert;

public class RepeatedAlertDecorator extends AlertDecorator{
    private int maxCount;
    private long interval;
    private AlertGenerator alertGenerator;

    public RepeatedAlertDecorator(Alert decoratedAlert, long interval, int maxCount, AlertGenerator alertGenerator){
        super(decoratedAlert);
        this.interval = interval;
        this.maxCount = maxCount;
        this.alertGenerator = alertGenerator;
    }

    public void triggerReapeatedAlerts(){
        for (int i = 0; i < maxCount; i++){
            try {
                Thread.sleep(interval); // this waits for the specified interval
                alertGenerator.triggerAlert(this.decoratedAlert); // this triggers the alert
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
