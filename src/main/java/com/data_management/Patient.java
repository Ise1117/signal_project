package com.data_management;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval
 * of medical records based on specified criteria.
 */
public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;

    /**
     * Constructs a new Patient with a specified ID.
     * Initializes an empty list of patient records.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds a new record to this patient's list of medical records.
     * The record is created with the specified measurement value, record type, and
     * timestamp.
     *
     * @param measurementValue the measurement value to store in the record
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since UNIX epoch
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    /**
     * Retrieves a list of PatientRecord objects for this patient that fall within a
     * specified time range.
     * The method filters records based on the start and end times provided.
     *
     * @param startTime the start of the time range, in milliseconds since UNIX
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since UNIX epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public ArrayList<PatientRecord> getRecords(long startTime, long endTime) {
        // TODO Implement and test this method
        ArrayList<PatientRecord> filteredRecords = new ArrayList<>();
            for(PatientRecord record : patientRecords){
                long timestamp = record.getTimestamp();
                if(timestamp >= startTime && timestamp <= endTime){
                    filteredRecords.add(record);
                }
            }
            return filteredRecords;
    }
    public int getId() {
        return this.patientId;
    }
    
    public ArrayList<PatientRecord> getRecordsLast(int count, String type) {
        ArrayList<PatientRecord> filtered = new ArrayList<>();
        for (int i = patientRecords.size() - 1; i >= 0; i--) {
            PatientRecord record = patientRecords.get(i);
            if (record.getRecordType().equalsIgnoreCase(type)) {
                filtered.add(record);
                if (filtered.size() == count) {
                    break;
                }
            }
        }
        ArrayList<PatientRecord> result = new ArrayList<>();
        for (int i = filtered.size() - 1; i >= 0; i--) {
            result.add(filtered.get(i));
        }
        return result;
    }
    public PatientRecord getLatest(String recordType) {
        PatientRecord latestRecord = null;
        for (PatientRecord record : patientRecords) {
            if (record.getRecordType().equalsIgnoreCase(recordType)) {
                if (latestRecord == null || record.getTimestamp() > latestRecord.getTimestamp()) {
                    latestRecord = record;
                }
            }
        }
        return latestRecord;
    }

    public ArrayList<PatientRecord> getRecordsLastMinute( String recordType) {
        
        ArrayList<PatientRecord> lastRecords = new ArrayList<>();
        if (patientRecords.size() == 0) {
            return null;
            
        } else {
            long timeNow = patientRecords.get(patientRecords.size()-1).getTimestamp();
            return getRecords(timeNow-10000*60, timeNow);
        }
    }
    public ArrayList<PatientRecord> getRecordsLastTenMinutes(String recordType) {
        ArrayList<PatientRecord> lastRecords = new ArrayList<>();
        if (patientRecords.size() == 0) {
            return null;
            
        } else {
            long timeNow = patientRecords.get(patientRecords.size()-1).getTimestamp();
            return getRecords(timeNow-10*60*1000, timeNow);
        }
    }
    public ArrayList<PatientRecord> getPatientRecords() {
        return new ArrayList<> (patientRecords);
    }
}

    