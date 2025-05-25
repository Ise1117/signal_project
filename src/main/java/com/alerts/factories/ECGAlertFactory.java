package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.ECGAlert;

/**
 * Factory class for creating ECGAlert instances.
 *
 * This class extends {@link AlertFactory} to provide a method for creating instances of the {@link ECGAlert} class.
 */
public class ECGAlertFactory extends AlertFactory {

    /**
     * Creates an ECGAlert instance with the specified patient ID and timestamp.
     *
     * @param patientId patient id
     * @param condition the condition associated with the alert which is noit used in this case.
     * @param timestamp the time at which the alert is created in milliseconds 
     * @return a new ECGAlert instance
     */
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new ECGAlert(String.valueOf(patientId), timestamp);
    }
}