package alert_tests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.alerts.alert_types.Alert;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import com.alerts.factories.HypoHypoxAlertFactory;

public class AlertFactoryTest {

    @Test
    void testBloodOxygenAlertFactory(){
        String patientId = "1";
        String condition = "Blood oxygen saturation alert";
        long timestamp = 1L;
        Alert alert = new BloodOxygenAlertFactory().createAlert(patientId, condition, timestamp);
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }
    
     @Test
    void testBloodPressureAlertFactory() {
        String patientId = "2";
        String condition = "Blood pressure alert";
        long timestamp = 2L;
        Alert alert = new BloodPressureAlertFactory().createAlert(patientId, condition, timestamp);
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }

    @Test
    void testHeartRateAlertFactory() {
        String patientId = "3";
        String condition = "Heart rate alert";
        long timestamp = 3L;
        Alert alert = new ECGAlertFactory().createAlert(patientId, condition, timestamp);
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }

    @Test
    void testHypoHypoxAlertFactory() {
        String patientId = "4";
        String condition = "Hypotensive hypoxemia alert";
        long timestamp = 4L;
        Alert alert = new HypoHypoxAlertFactory().createAlert(patientId, condition, timestamp);
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }
}
