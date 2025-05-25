package com.alerts;

/**
 * Represents an alert specific to blood oxygen levels.
 *
 * This class extends the {@link Alert} class to provide functionality specific
 * to blood oxygen level alerts.
 */
public class BloodOxygenAlert extends Alert {

    /**
     * Constructs a BloodOxygenAlert with the specified patient ID, condition, and timestamp.
     *
     * @param patientId the patient id
     * @param condition the condition associated with the alert
     * @param timestamp the time at which the alert is created, in milliseconds
     */
    public BloodOxygenAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}