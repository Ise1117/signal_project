package com.alerts;

/**
 * Represents an alert specific to electrocardiogram (ECG) readings.
 *
 * This class extends the {@link Alert} class to provide functionality specific
 * to ECG alerts.
 */
public class ECGAlert extends Alert {

    /**
     * Constructs an ECGAlert with the specified patient ID and timestamp.
     *
     * @param patientId the patient id
     * @param timestamp the time at which the alert is created
     */
    public ECGAlert(String patientId, long timestamp) {
        super(patientId, "ECG Alert", timestamp);
    }
}