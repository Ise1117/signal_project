package data_management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.DataReader;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

class DataStorageTest {

    @Mock
    private DataReader reader;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddAndGetRecords() throws IOException {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
        // DataReader reader
        List<PatientRecord> mockRecords = new ArrayList<>();
        mockRecords.add(new PatientRecord(1, 100.0, "WhiteBloodCells", 1714376789050L));
        mockRecords.add(new PatientRecord(1, 200.0, "WhiteBloodCells", 1714376789051L));

        Answer<Void> answer = invocation -> {
            DataStorage dataStorage = invocation.getArgument(0);
            for (PatientRecord record : mockRecords) {
                dataStorage.addPatientData(record.getPatientId(), record.getMeasurementValue(), 
                    record.getRecordType(), record.getTimestamp());
            }
            return null;
        };

        doAnswer(answer).when(reader).readData(any(DataStorage.class));

        DataStorage storage = new DataStorage(reader);

        reader.readData(storage);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);

        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        assertEquals(200.0, records.get(1).getMeasurementValue()); // Validate second record
    }
}
