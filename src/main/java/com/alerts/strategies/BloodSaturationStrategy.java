package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class BloodSaturationStrategy implements AlertStrategy {
    public static final int saturation = 92;

    
    @Override
    public Alert checkAlert(Patient patient) {
      try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
      PatientRecord lastRecord = null;
      BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();
      List<PatientRecord> lastSaturationRecordLists = patient.getRecordsLast(1, "Saturation");
      if (lastSaturationRecordLists.size() == 0) {
        return null;
        
      } else {
        lastRecord= lastSaturationRecordLists.get(0);
      }
      if (lastRecord != null) {
        if(lastRecord.getMeasurementValue() < saturation) {
        return factory.createAlert(patient.getId(), "Low Saturation Alert", System.currentTimeMillis());
      }
    }

      List<PatientRecord> lastTen = patient.getRecordsLastTenMinutes( "Saturation");
    
      if (Math.abs(lastTen.get(0).getMeasurementValue() - lastTen.get(lastTen.size()-1).getMeasurementValue()) >lastTen.get(0).getMeasurementValue()*0.05 ) {
            return factory.createAlert(patient.getId(), "Saturation Drop", System.currentTimeMillis());
          }
      

      return null;
    }
}