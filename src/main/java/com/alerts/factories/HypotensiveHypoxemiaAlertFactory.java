package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.HypotensiveHypoxemiaAlert;

/**
 * Factory class for creating HypotensiveHypoxemiaAlert instances.
 *
 * This class extends {@link AlertFactory} to provide a method for creating instances of the {@link HypotensiveHypoxemiaAlert} class.
 */
public class HypotensiveHypoxemiaAlertFactory extends AlertFactory {

    /**
     * Creates a HypotensiveHypoxemiaAlert instance with the specified patient ID and timestamp.
     *
     * @param patientId the patient ID
     * @param condition the condition associated with the alert whcih is also not used in this case
     * @param timestamp the time at which the alert is created in miliseconds
     * @return a new HypotensiveHypoxemiaAlert instance
     */
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new HypotensiveHypoxemiaAlert(String.valueOf(patientId), timestamp);
    }
}