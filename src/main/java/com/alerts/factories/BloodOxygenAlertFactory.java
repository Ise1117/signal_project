package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlert;

/**
 * Factory class for creating BloodOxygenAlert instances.
 *
 * This class extends {@link AlertFactory} to provide a method for creating instances of the {@link BloodOxygenAlert} class.
 */
public class BloodOxygenAlertFactory extends AlertFactory {

    /**
     * Creates a BloodOxygenAlert instance with the specified patientId , condition asnd timestamp.
     *
     * @param patientId the id of the patient
     * @param condition the condition associated with the alert
     * @param timestamp the time at which the alert is created
     * @return a new BloodOxygenAlert instance
     */
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(String.valueOf(patientId), condition, timestamp);
    }
}