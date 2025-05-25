package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class AlertGenerator {

    private final DataStorage dataStorage;
    public ArrayList<Alert> triggeredAlerts;

    public AlertGenerator(DataStorage dataStorage) {
        this.triggeredAlerts = new ArrayList<>();
        this.dataStorage = dataStorage;
    }

    public void evaluateData(Patient patient) {
        checkBloodPressureAlerts(patient);
        checkSaturationAlerts(patient);
        checkHypotensiveHypoxemia(patient);
        checkECGAlerts(patient);
    }

    private void triggerAlert(Alert alert) {
        System.out.println("ALERT TRIGGERED:");
        System.out.println("Patient ID: " + alert.getPatientId());
        System.out.println("Condition: " + alert.getCondition());
        System.out.println("Timestamp: " + alert.getTimestamp());
        triggeredAlerts.add(alert);
    }

    private void checkBloodPressureAlerts(Patient patient) {
        List<PatientRecord> systolicRecords = patient.getRecordsLast(3, "Systolic");
        List<PatientRecord> diastolicRecords = patient.getRecordsLast(3, "Diastolic");
        long timestamp = System.currentTimeMillis();

        if (systolicRecords.size() == 3 && diastolicRecords.size() == 3) {
            int[] systolic = new int[3];
            int[] diastolic = new int[3];

            for (int i = 0; i < 3; i++) {
                systolic[i] = (int) systolicRecords.get(i).getMeasurementValue();
                diastolic[i] = (int) diastolicRecords.get(i).getMeasurementValue();
            }

            // Trigger a single "Critical Blood Pressure" alert for any critical threshold
            for (int i = 0; i < 3; i++) {
                if (systolic[i] > 180 || systolic[i] < 90 || diastolic[i] > 120 || diastolic[i] < 60) {
                    triggerAlert(new Alert(String.valueOf(patient.getId()), "Critical Blood Pressure", timestamp));
                    break;  // one alert is enough for critical condition
                }
            }

            // Check trends as before (using exact strings expected by your tests)
            if ((systolic[0] + 10 < systolic[1]) && (systolic[1] + 10 < systolic[2])) {
                triggerAlert(new Alert(String.valueOf(patient.getId()), "Increasing Systolic Trend", timestamp));
            }
            if ((diastolic[0] + 10 < diastolic[1]) && (diastolic[1] + 10 < diastolic[2])) {
                triggerAlert(new Alert(String.valueOf(patient.getId()), "Increasing Diastolic Trend", timestamp));
            }
            if ((systolic[0] - 10 > systolic[1]) && (systolic[1] - 10 > systolic[2])) {
                triggerAlert(new Alert(String.valueOf(patient.getId()), "Decreasing Systolic Trend", timestamp));
            }
            if ((diastolic[0] - 10 > diastolic[1]) && (diastolic[1] - 10 > diastolic[2])) {
                triggerAlert(new Alert(String.valueOf(patient.getId()), "Decreasing Diastolic Trend", timestamp));
            }
        }
    }

    private void checkSaturationAlerts(Patient patient) {
        List<PatientRecord> records = patient.getRecordsLast(10, "Saturation");
        long now = System.currentTimeMillis();
        long tenMinutesAgo = now - (10 * 60 * 1000);

        double latest = -1;
        double earlier = -1;

        for (int i = records.size() - 1; i >= 0; i--) {
            PatientRecord record = records.get(i);
            double value = record.getMeasurementValue();
            long ts = record.getTimestamp();

            if (latest == -1) {
                latest = value;
            }

            if (ts <= tenMinutesAgo) {
                earlier = value;
                break;
            }
        }

        if (latest < 92) {
            triggerAlert(new Alert(String.valueOf(patient.getId()), "Low Saturation (<92%)", now));
        }

        if (earlier != -1 && (earlier - latest) >= 5) {
            triggerAlert(new Alert(String.valueOf(patient.getId()), "Rapid Saturation Drop (≥5%)", now));
        }
    }

    private void checkHypotensiveHypoxemia(Patient patient) {
        // IMPORTANT: Use same measurement keys as in other BP checks
        PatientRecord systolicRecord = patient.getLatest("Systolic");
        PatientRecord satRecord = patient.getLatest("Saturation");

        if (systolicRecord == null || satRecord == null) {
            return;
        }

        int systolic = (int) systolicRecord.getMeasurementValue();
        double saturation = satRecord.getMeasurementValue();

        if (systolic < 90 && saturation < 92) {
            triggerAlert(new Alert(String.valueOf(patient.getId()), "Hypotensive Hypoxemia Alert", System.currentTimeMillis()));
        }
    }

    private void checkECGAlerts(Patient patient) {
        List<PatientRecord> ecgRecords = patient.getRecordsLast(20, "ECG");
        if (ecgRecords.size() < 5) return;

        double sum = 0;
        int count = 0;
        double peakThreshold = 2.0;

        for (PatientRecord record : ecgRecords) {
            double val = record.getMeasurementValue();
            sum += val;
            count++;
        }

        double avg = sum / count;
        double latest = ecgRecords.get(ecgRecords.size() - 1).getMeasurementValue();

        if (latest > avg * peakThreshold) {
            triggerAlert(new Alert(String.valueOf(patient.getId()), "Abnormal ECG Peak", System.currentTimeMillis()));
        }
    }
}
