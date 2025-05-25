package com.alerts.factories;
import com.alerts.Alert;
/**
 * This is the factory class for creating instances for alerts
 * It provides a method for creating instances of the {@link Alert} class with the patient ID, condition and timestamp.
 */
public class AlertFactory {
    /**
     * creates an alert instance w the patient ID condition and timestamp.
     * @param patientId the id of the patient
     * @param condition is the condition assosciated with the alert
     * @param timestamp is the time at which the alert is created in miliseconds
     * @return a new Alert
     */
    public Alert createAlert(int patientId, String condition, long timestamp){
        return new Alert(String.valueOf(patientId), condition, timestamp);
    }
    
}
