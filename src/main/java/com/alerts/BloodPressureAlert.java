package com.alerts;

/**
 * Represents an alert specific to blood pressure levels.
 *
 * This class extends the {@link Alert} class to provide functionality specific
 * to blood pressure level alerts.
 */
public class BloodPressureAlert extends Alert {

    /**
     * Constructs a BloodPressureAlert with the specified patient id and timestamp.
     *
     * @param patientId the id of the patient
     * @param timestamp the time at which the alert is created in milliseconds
     */
    public BloodPressureAlert(String patientId, long timestamp) {
        super(patientId, "Blood Pressure Alert", timestamp);
    }
}