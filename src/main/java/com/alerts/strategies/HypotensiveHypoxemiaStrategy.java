package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.HypotensiveHypoxemiaAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.ArrayList;

public class HypotensiveHypoxemiaStrategy implements AlertStrategy {

    public static final int systolic = 90;
    public static final int saturation = 92;


    @Override
    public Alert checkAlert(Patient patient) {
      boolean lowSystolic = false;
      boolean lowSaturation = false;

      PatientRecord lastRecord = null;

      ArrayList<PatientRecord> lastSystolicRecords = patient.getRecordsLast(1, "SystolicPressure");
        if(lastSystolicRecords.size() == 0) {
            return null;
        }
        else {
            lastRecord = lastSystolicRecords.get(0);
        }
          
      if (lastRecord != null) {
        if ( lastRecord.getMeasurementValue() <  systolic) {
        lowSystolic = true;
        }
      }

      ArrayList<PatientRecord>  lastSaturationRecords = patient.getRecordsLast(1, "Saturation");
      if(lastSaturationRecords.size() == 0) {
        return null;
      } else {
        lastRecord = lastSaturationRecords.get(0);
      }
      if (lastRecord != null) {
        if( lastRecord.getMeasurementValue() < saturation)  
                lowSaturation = true;
      }
      if (lowSystolic && lowSaturation) {
        HypotensiveHypoxemiaAlertFactory factory = new HypotensiveHypoxemiaAlertFactory();
        return factory.createAlert(patient.getId(),"HypotensiveHypoxemia Alert", System.currentTimeMillis());
      }
        
    return null;
}
}