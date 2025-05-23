package com.cardio_generator.outputs;
/**]
 * The {@code OutputStrategy} class is outputting patient data, using {@code output}
 */

public interface OutputStrategy {
    /**
     * Function is outputting data for a patient
     * @param patientId is the patient ID
     * @param timestamp is the timestamp of the record
     * @param label is the datas label
     * @param data is the data for the patient
     */
    void output(int patientId, long timestamp, String label, String data);
}
