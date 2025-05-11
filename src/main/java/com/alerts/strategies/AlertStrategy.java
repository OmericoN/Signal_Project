package com.alerts.strategies;
import com.alerts.AlertGenerator;
import com.data_management.Patient;

public interface AlertStrategy {
    public void checkAlert(Patient patient, AlertGenerator alertGenerator);
}
