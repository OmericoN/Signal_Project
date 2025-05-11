package com.alerts.alert_types;

public interface Alert {
    public String getPatientId();
    public String getCondition();
    public long getTimestamp();
    
}
