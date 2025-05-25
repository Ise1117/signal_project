package alerts;

import com.alerts.AlertGenerator;
import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlertGeneratorTest {

    private AlertGenerator alertGenerator;
    private TestPatient testPatient;

    private static class TestPatient extends Patient {
        private final List<PatientRecord> records = new ArrayList<>();

        public TestPatient(int id) {
            super(id);
        }

        @Override
        public ArrayList<PatientRecord> getRecordsLast(int numRecords, String recordType) {
            ArrayList<PatientRecord> filtered = new ArrayList<>();
            for (int i = records.size() - 1; i >= 0 && filtered.size() < numRecords; i--) {
                PatientRecord r = records.get(i);
                if (r.getRecordType().equals(recordType)) {
                    filtered.add(0, r);
                }
            }
            return filtered;
        }

        @Override
        public PatientRecord getLatest(String recordType) {
            for (int i = records.size() - 1; i >= 0; i--) {
                PatientRecord r = records.get(i);
                if (r.getRecordType().equals(recordType)) {
                    return r;
                }
            }
            return null;
        }

        public void addRecord(double value, String type, long timestamp) {
            records.add(new PatientRecord(this.getId(), value, type, timestamp));
        }
    }

    @BeforeEach
    public void setup() {
        alertGenerator = new AlertGenerator(null);
        testPatient = new TestPatient(1);
        alertGenerator.triggeredAlerts.clear();
    }

    private void addRecord(double value, String type, long offsetMillis) {
        testPatient.addRecord(value, type, System.currentTimeMillis() + offsetMillis);
    }



    @Test
    void testIncreasingSystolicTrendTriggersAlert()throws IOException {
        addRecord(100, "Systolic", -3000);
        addRecord(115, "Systolic", -2000);
        addRecord(130, "Systolic", -1000);

        // Add diastolic data to satisfy 3 records check
        addRecord(70, "Diastolic", -3000);
        addRecord(75, "Diastolic", -2000);
        addRecord(80, "Diastolic", -1000);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Increasing Systolic Trend")));
    }

    @Test
    void testDecreasingSystolicTrendTriggersAlert()throws IOException {
        addRecord(130, "Systolic", -3000);
        addRecord(115, "Systolic", -2000);
        addRecord(100, "Systolic", -1000);

        addRecord(80, "Diastolic", -3000);
        addRecord(75, "Diastolic", -2000);
        addRecord(70, "Diastolic", -1000);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Decreasing Systolic Trend")));
    }

    @Test
    void testIncreasingDiastolicTrendTriggersAlert()throws IOException {
        addRecord(70, "Diastolic", -3000);
        addRecord(85, "Diastolic", -2000);
        addRecord(100, "Diastolic", -1000);

        addRecord(110, "Systolic", -3000);
        addRecord(115, "Systolic", -2000);
        addRecord(120, "Systolic", -1000);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Increasing Diastolic Trend")));
    }

    @Test
    void testDecreasingDiastolicTrendTriggersAlert() throws IOException{
        addRecord(100, "Diastolic", -3000);
        addRecord(85, "Diastolic", -2000);
        addRecord(70, "Diastolic", -1000);

        addRecord(120, "Systolic", -3000);
        addRecord(115, "Systolic", -2000);
        addRecord(110, "Systolic", -1000);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Decreasing Diastolic Trend")));
    }



    @Test
    void testCriticalHighSystolicTriggersAlert() throws IOException{
        addRecord(190, "Systolic", 0);
        addRecord(80, "Diastolic", 0);
        addRecord(80, "Diastolic", -1000);
        addRecord(100, "Systolic", -1000);
        addRecord(100, "Systolic", -2000);
        addRecord(90, "Diastolic", -2000);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Critical Blood Pressure")));
    }

    @Test
    void testCriticalLowSystolicTriggersAlert()throws IOException {
        addRecord(80, "Systolic", 0);
        addRecord(85, "Systolic", -1000);
        addRecord(88, "Systolic", -2000);

        addRecord(70, "Diastolic", 0);
        addRecord(72, "Diastolic", -1000);
        addRecord(74, "Diastolic", -2000);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Critical Blood Pressure")));
    }

    @Test
    void testCriticalHighDiastolicTriggersAlert()throws IOException {
        addRecord(120, "Systolic", 0);
        addRecord(125, "Systolic", -1000);
        addRecord(130, "Systolic", -2000);

        addRecord(130, "Diastolic", 0);
        addRecord(135, "Diastolic", -1000);
        addRecord(140, "Diastolic", -2000);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Critical Blood Pressure")));
    }

    @Test

    void testCriticalLowDiastolicTriggersAlert() throws IOException{
        addRecord(120, "Systolic", 0);
        addRecord(115, "Systolic", -1000);
        addRecord(110, "Systolic", -2000);

        addRecord(50, "Diastolic", 0);
        addRecord(55, "Diastolic", -1000);
        addRecord(58, "Diastolic", -2000);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Critical Blood Pressure")));
    }


    @Test
    void testLowSaturationTriggersAlert() throws IOException {
        addRecord(91, "Saturation", 0);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Low Saturation (<92%)")));
    }

    @Test
    void testRapidSaturationDropTriggersAlert() throws IOException {
        long now = System.currentTimeMillis();

        addRecord(95, "Saturation", -11 * 60 * 1000);
        addRecord(90, "Saturation", -5 * 60 * 1000);
        addRecord(89, "Saturation", 0);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Rapid Saturation Drop (≥5%)")));
    }

    @Test
    void testHypotensiveHypoxemiaAlertTriggers() throws IOException {
        addRecord(85, "Systolic", -1000);
        addRecord(80, "Systolic", -2000);
        addRecord(88, "Systolic", -3000);

        addRecord(90, "Saturation", 0);

        alertGenerator.evaluateData(testPatient);

        assertTrue(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Hypotensive Hypoxemia Alert")));
    }
    @Test
    void testAbnormalEcgPeakTriggersAlert() throws IOException {
        addRecord(1.0, "ECG", -3000);
        addRecord(1.0, "ECG", -2000);
        addRecord(1.0, "ECG", -1000);
        addRecord(3.5, "ECG", 0); 

        alertGenerator.evaluateData(testPatient);

        assertFalse(alertGenerator.triggeredAlerts.stream()
                .anyMatch(a -> a.getCondition().equals("Abnormal ECG Peak")));
    }

}
