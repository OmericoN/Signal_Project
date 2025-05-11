package com.alerts.alert_types;

public class ECGAlert implements Alert{
    private String patientId;
    private String condition;
    private long timestamp;

    public ECGAlert(String patientId, String condition, long timestamp){
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }
    @Override
    public String getPatientId(){
        return this.patientId;
    }
    @Override
    public String getCondition(){
        return this.condition;
    }
    @Override
    public long getTimestamp(){
        return this.timestamp;
    }
}
