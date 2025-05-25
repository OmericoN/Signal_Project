package data_management;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
        // DataReader reader
        // DataStorage storage = new DataStorage();   // No longer used because of singleton design pattern
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);
    
        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
    }

    @Test
    void testAddAndRetrieveMultiplePatients(){
        // DataStorage storage = new DataStorage();   // No longer used because of singleton design pattern
        DataStorage storage = DataStorage.getInstance();
        long now = System.currentTimeMillis();
        
        storage.addPatientData(1, 120.0, "SystolicPressure", now - 1000);
        storage.addPatientData(1, 122.0, "SystolicPressure", now);
        storage.addPatientData(1, 125.0, "SystolicPressure", now + 1000);
        
        assertEquals(1, storage.getRecords(1, now - 1100, now - 900).size());
        assertEquals(1, storage.getRecords(1, now - 100, now + 100).size());
        assertEquals(1, storage.getRecords(1, now + 900, now + 1100).size());
        assertEquals(3, storage.getRecords(1, now - 1100, now + 1100).size());
        assertEquals(0, storage.getRecords(1, now + 2000, now + 3000).size());
    }

    @Test
    void testRetrieveDataForNonExisting(){
        // DataStorage storage = new DataStorage();   // No longer used because of singleton design pattern
        DataStorage storage = DataStorage.getInstance();
        List<PatientRecord> nonExistentPatientRecords = storage.getRecords(999, 0L, 1000L);
        assertEquals(0, nonExistentPatientRecords.size());
    }

    //This test is no longer needed since we dicontinue the readData() function from week 5
//     @Test
//     void testWithMockDataReader() {
//         // Testing with mockreader to simulate behavior of the DataReader that I created
//         DataReader mockReader = new DataReader() {
//             @Override
//             public void readData(DataStorage storage) {
//                 storage.addPatientData(1, 120.0, "SystolicPressure", 1000L);
//                 storage.addPatientData(1, 80.0, "DiastolicPressure", 1100L);
//             }
//         };
//         // DataStorage storage = new DataStorage();   // No longer used because of singleton design pattern
//         DataStorage storage = DataStorage.getInstance();
//         try {
//             mockReader.readData(storage);
//         } catch (IOException e) {
//             e.printStackTrace();
//             throw new RuntimeException("IOException occurred during mockReader.readData", e);
//         }
        
//         assertEquals(2, storage.getRecords(1, 0L, 2000L).size());
// }
}