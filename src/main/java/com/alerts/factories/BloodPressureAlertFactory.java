package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.BloodPressureAlert;

/**
 * Factory class for creating BloodPressureAlert instances.
 *
 * This class extends {@link AlertFactory} to provide a method for creating instances of the {@link BloodPressureAlert} class.
 */
public class BloodPressureAlertFactory extends AlertFactory {

    /**
     * Creates a BloodPressureAlert instance with the specified patient ID and timestamp.
     *
     * @param patientId the patient id
     * @param condition the condition associated with the alert which is not used in this case
     * @param timestamp the time at which the alert is created in milliseconds 
     * @return a new BloodPressureAlert instance
     */
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new BloodPressureAlert(String.valueOf(patientId), timestamp);
    }
}