package data_management;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.WebSocketClientReader;

public class WebSocketClientReaderTest {

    private WebSocketOutputStrategy server;
    private WebSocketClientReader client;
    private DataStorage storage;
    private static int portCounter = 9010;

    /**
     * sets up the tests with a websocket of port 9001 and URI, also sets up data storage and initiates stream
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        int port = portCounter++;
        server = new WebSocketOutputStrategy(port);
        storage = DataStorage.getInstance();
        client = new WebSocketClientReader(new URI("ws://localhost:" + port));
        client.startStream(storage);
        Thread.sleep(500);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (client != null) {
        client.stopStream();
        }
        if (server != null){
            server.stop();
            Thread.sleep(500);
        }
        DataStorage.getInstance().clear(); // This wipes all stored data after each test;
        
    }


    // Case - Check duplicates handling case
    @Test
    void testDuplicateDataStorage() throws Exception {
        int patientId = 6;
        long timestamp = System.currentTimeMillis();
        String label = "SystolicPressure";
        String measurement = "150.0";
        server.output(patientId, timestamp, label, measurement);
        Thread.sleep(1000);
        server.output(patientId, timestamp, label, measurement);

        Thread.sleep(500); //to ensure that the message gets processed

        List<PatientRecord> records = storage.getRecords(patientId, 1, Long.MAX_VALUE);
        assertEquals(1, records.size());
    }

    //Case - Check that data is parsed and stored correctly
    @Test
    void testParsedDataIsCorrect() throws Exception {
        int patientId = 2;
        long timestamp = System.currentTimeMillis();
        String label = "Saturation";
        String measurement = "98.0";
        server.output(patientId, timestamp, label, measurement);
    
        Thread.sleep(500); 
    
        List<PatientRecord> records = storage.getRecords(patientId, 1, Long.MAX_VALUE);
        assertEquals(1, records.size());
    
        // Check the actual content of the record
        PatientRecord record = records.get(0);
        assertEquals(patientId, record.getPatientId());
        assertEquals(label, record.getRecordType());
        assertEquals(timestamp, record.getTimestamp());
        assertEquals(measurement, String.valueOf(record.getMeasurementValue()));
    
    }

    //Case - check that malformed messages are gracefully handled
    @Test
    void testCompletelyMalformedMessage() throws Exception {
        int patientId = 3;
        long timestamp = System.currentTimeMillis();
        String malformedMessage = "this_is_not_a_valid_message";

        client.onMessage(malformedMessage);

        Thread.sleep(500);

        List<PatientRecord> records = storage.getRecords(patientId, 1, Long.MAX_VALUE);
        assertEquals(0, records.size());
    }

    //Case - message with 4 fields but invalid formats
    @Test
    void testInvalidMessageFormat() throws Exception {
        int patientId = 4;
        long timestamp = System.currentTimeMillis();
        // the patientId is not a number which should cause parsing to fail
        String invalidMessage = "notANumber," + timestamp + ",ECG,5.0";
        client.onMessage(invalidMessage);
    
        // Or the timestamp is not a number
        String invalidMessage2 = patientId + ",notATimestamp,ECG,5.0";
        client.onMessage(invalidMessage2);
    
        // Or the measurement is not a number
        String invalidMessage3 = patientId + "," + timestamp + ",ECG,notANumber";
        client.onMessage(invalidMessage3);
    
        Thread.sleep(500);
    
        // None of these should be stored
        List<PatientRecord> records = storage.getRecords(patientId, 1, Long.MAX_VALUE);
        assertEquals(0, records.size());
    }

    //Start of Integration Testing

    //Case - multiple messages
    @Test
    void testMultipleMessages() throws Exception {
        int patientId = 5;
        long t1 = System.currentTimeMillis();
        long t2 = t1 + 100;
        server.output(patientId, t1, "SystolicPressure", "120.0");
        server.output(patientId, t2, "DiastolicPressure", "80.0");

        Thread.sleep(500);

        List<PatientRecord> records = storage.getRecords(patientId, 1, Long.MAX_VALUE);
        assertEquals(2, records.size());
    }
}

