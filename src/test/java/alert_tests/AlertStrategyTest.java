package alert_tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.alerts.strategies.AlertStrategy;
import com.alerts.strategies.BloodOxygenStrategy;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.HypoHypoxStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class AlertStrategyTest {

    private static final long CURRENT_TIME = System.currentTimeMillis();
    private AlertGenerator alertGenerator;
    private Patient testPatient;

    @BeforeEach
    public void setUp() {
        DataStorage dataStorage = DataStorage.getInstance();
        alertGenerator = new AlertGenerator(dataStorage);
        testPatient = new Patient(1);
    }

    @Test
    void testBloodOxygenStrategy() {
        AlertStrategy strategy = new BloodOxygenStrategy();

        testPatient.addRecord(90.0, "Saturation", CURRENT_TIME);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));

        strategy.checkAlert(testPatient, alertGenerator);

        System.setOut(old);
        assertTrue(out.toString().contains("Blood saturation dropped below 92%"));
    }

    @Test
    void testBloodPressureStrategy() {
        AlertStrategy strategy = new BloodPressureStrategy();
        testPatient.addRecord(185.0, "SystolicPressure", CURRENT_TIME); 
        testPatient.addRecord(50.0, "DiastolicPressure", CURRENT_TIME); 

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        strategy.checkAlert(testPatient, alertGenerator);

        System.setOut(old);

        assertTrue(out.toString().contains("Critical systolic-pressure"));
        assertTrue(out.toString().contains("Critical diastolic-pressure"));
    }

    @Test
    void testHeartRateStrategy() {
        AlertStrategy strategy = new HeartRateStrategy();
        for (int i = 0; i < 5; i++) {
            testPatient.addRecord(1.0, "ECG", CURRENT_TIME - (i * 1000));
        }
        testPatient.addRecord(5.0, "ECG", CURRENT_TIME); 

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));

        strategy.checkAlert(testPatient, alertGenerator);
        System.setOut(old);

        assertTrue(out.toString().contains("Abnormal ECG reading detected"));
    }

    @Test
    void testHypoHypoxStrategy() {
        AlertStrategy strategy = new HypoHypoxStrategy();

        testPatient.addRecord(85.0, "SystolicPressure", CURRENT_TIME); 
        testPatient.addRecord(90.0, "Saturation", CURRENT_TIME); 

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));

        strategy.checkAlert(testPatient, alertGenerator);
        System.setOut(old);
        assertTrue(out.toString().contains("Hypotensive Hypoxemia Alert"));
    }
}