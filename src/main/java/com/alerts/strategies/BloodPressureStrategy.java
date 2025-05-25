package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    public static final int trend = 10;
    public static final int diastolic_high = 180;
    public static final int diastolic_low = 90;
    public static final int systolic_high = 120;
    public static final int systolic_low= 60;

    public Alert checkTrend( String type, Patient patient, BloodPressureAlertFactory factory) {
      List<PatientRecord> lastRecords = patient.getRecordsLast(3, type);
      boolean isIncreasing = true;
      boolean isDecreasing = true;
      if (lastRecords.size() == 3) {
        PatientRecord lastRecord = lastRecords.get(0);
        for(int i=1; i<3; i++){
          if(lastRecords.get(i).getMeasurementValue() - lastRecord.getMeasurementValue() <= trend){
            isIncreasing = false;
          }
          if(lastRecords.get(i).getMeasurementValue() - lastRecord.getMeasurementValue() >= -trend){
            isDecreasing = false;
          }
        }
        if(isIncreasing)
            return factory.createAlert(patient.getId(), "Blood Pressure Increasing Alert", lastRecord.getTimestamp());
        if(isDecreasing)
            return factory.createAlert(patient.getId(), "Blood Pressure Decreasing Alert", lastRecord.getTimestamp());
        return null;
      }
      return null;
    }

    @Override
    public Alert checkAlert(Patient patient) {
      
      BloodPressureAlertFactory factory = new BloodPressureAlertFactory();
      Alert alert = checkTrend("SystolicPressure", patient, factory);
      Alert alert2 = checkTrend("DiastolicPressure", patient, factory);
      if(alert == null && alert2 == null) {
        
      } else if(alert == null) {
          return alert2;
      } else if(alert2 == null) {
        return alert;
      }  else {
    
        String message1 = alert.getCondition();
        String message2 = alert2.getCondition();

        return factory.createAlert(patient.getId(), "Systolic: " + message1 + " Diastolic: " + message2, trend);
      } 
      
      List<PatientRecord> lastRecordsSys = patient.getRecordsLast(1, "SystolicPressure");
      PatientRecord lastRecordSys = null;
      PatientRecord lastRecordDia = null;
      if (lastRecordsSys.size() == 0) {
        
        return null;
        
      } else {
        lastRecordSys = lastRecordsSys.get(0);
      }
      if (lastRecordSys != null) {
        
        if (lastRecordSys.getMeasurementValue() > systolic_high) {
          
          return factory.createAlert(patient.getId(), "High Systolic Blood Pressure Alert",
              lastRecordSys.getTimestamp());
        }
        if (lastRecordSys.getMeasurementValue() < systolic_low) {
          return factory.createAlert(patient.getId(), "Low Systolic Blood Pressure Alert",
              lastRecordSys.getTimestamp());
        }
      }
      List<PatientRecord> lastRecordsDia = patient.getRecordsLast(1, "DiastolicPressure");
      if (lastRecordsDia.size() == 0) {
        return null;
        
      } else {
        lastRecordDia = lastRecordsDia.get(0);
      }
      
      if (lastRecordDia != null) {
        if (lastRecordDia.getMeasurementValue() > diastolic_high) {
          return factory.createAlert(patient.getId(), "High Diastolic Blood Pressure Alert",
              lastRecordDia.getTimestamp());
        }
        if (lastRecordDia.getMeasurementValue() < diastolic_low) {
          return factory.createAlert(patient.getId(), "Low Diastolic Blood Pressure Alert",
              lastRecordDia.getTimestamp());
        }
      }
      
      return null;
    }
    
}