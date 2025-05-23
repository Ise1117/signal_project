package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code PatientDataGenerator} class generates a record for a certain patient using {@code generate}
 */

public interface PatientDataGenerator {
    /**
     * Functipon generates data for the patient
     * @param patientId is the patient ID
     * @param outputStrategy is the output strategy chosen.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
