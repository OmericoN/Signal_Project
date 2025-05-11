package alert_tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class AlertGeneratorTest { //This Test also assesses the AlertStrategies since the AlertGenerator was modified to utilize these strategies
    private static final long CURRENT_TIME = System.currentTimeMillis();
    private AlertGenerator alertGenerator;
    private DataStorage dataStorage;
    private Patient testPatient;

    @BeforeEach
    public void setUp() {
        //dataStorage = new DataStorage();     //No longer used because of singleton design pattern
        DataStorage dataStorage = DataStorage.getInstance();
        alertGenerator = new AlertGenerator(dataStorage);
        testPatient = new Patient(1);
    }

    @Test
    public void testAbnormalRealativeToAvgECGDetection(){
        for (int i = 0; i < 5; i++){
            testPatient.addRecord(1.0, "ECG", CURRENT_TIME - (i*1000));
        }
        testPatient.addRecord(5.0, "ECG", CURRENT_TIME);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("ECG"));
    }

    @Test
     public void testSystolicPressureAboveThreshold(){
        // Test high systolic pressure
        testPatient.addRecord(185.0, "SystolicPressure", CURRENT_TIME);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("systolic-pressure") && out.toString().contains("outside threshold"));
    }
    @Test
    public void testSystolicPressureBelowThreshold(){
        // Test low systolic pressure
        testPatient.addRecord(80.0, "SystolicPressure", CURRENT_TIME);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("systolic-pressure") && out.toString().contains("outside threshold"));
    }

    @Test
    public void testDiastolicPressureAboveThreshold(){
        // Test high diastolic pressure
        testPatient.addRecord(125.0, "DiastolicPressure", CURRENT_TIME);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("diastolic-pressure") && out.toString().contains("outside threshold"));
    }
    @Test
    public void testDiastolicPressureBelowThreshold(){
        // Test low diastolic pressure
        testPatient.addRecord(50.0, "DiastolicPressure", CURRENT_TIME);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("diastolic-pressure") && out.toString().contains("outside threshold"));
    }

    @Test
    public void testSystolicPrssureCriticalChange(){
        // Tests critical change in systolic pressure consecutively
        testPatient.addRecord(100, "SystolicPressure", 1L);
        testPatient.addRecord(115, "SystolicPressure", 2L);
        testPatient.addRecord(130, "SystolicPressure", 3L);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("systolic-pressure") && out.toString().contains("trend"));
    }

    @Test
    public void testDiastolicPressureCriticalChange(){
        // Tests critical change in diastolic pressure consecutively
        testPatient.addRecord(135, "DiastolicPressure", 1L);
        testPatient.addRecord(115, "DiastolicPressure", 2L);
        testPatient.addRecord(100, "DiastolicPressure", 3L);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("diastolic-pressure") && out.toString().contains("trend"));

    }

    @Test 
    void testLowBloodSaturation(){
        // Test blood saturation under threshold alert
        testPatient.addRecord(90.0, "Saturation", CURRENT_TIME);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("saturation") && out.toString().contains("dropped") && out.toString().contains("below") && out.toString().contains("92%"));
    }

    @Test
    void testCriticalBloodSaturationDecline(){
        // Test a 5% decline in blood oxygen saturation
        testPatient.addRecord(99.0, "Saturation", 1L);
        testPatient.addRecord(92, "Saturation", 2L);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("blood") && out.toString().contains("saturation") && out.toString().contains("decline") && out.toString().contains("Rapid"));

    }

    @Test
    void testCheckHypotHypox(){
        //Test Hypotensive Hypoxemia Condition alert
        testPatient.addRecord(85.0, "SystolicPressure", 1L);
        testPatient.addRecord(90.0, "Saturation", 2L);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        alertGenerator.evaluateData(testPatient);
        System.setOut(old);
        assertTrue(out.toString().contains("Hypotensive") && out.toString().contains("Hypoxemia") && out.toString().contains("Alert"));
    }
}
