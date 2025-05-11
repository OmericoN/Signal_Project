package alert_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.alerts.alert_types.Alert;
import com.alerts.alert_types.BloodPressureAlert;
import com.alerts.decorators.AlertDecorator;
import com.alerts.decorators.LoggingAlertDecorator;
import com.alerts.decorators.PriorityAlertDecorator;

public class AlertDecoratorTest {

    @Test
    void testBasicAlertDecorator() {
        Alert baseAlert = new BloodPressureAlert("1", "High Blood Pressure", System.currentTimeMillis());

        AlertDecorator decoratedAlert = new AlertDecorator(baseAlert);

        assertEquals(baseAlert.getPatientId(), decoratedAlert.getPatientId());
        assertEquals(baseAlert.getCondition(), decoratedAlert.getCondition());
        assertEquals(baseAlert.getTimestamp(), decoratedAlert.getTimestamp());
    }

    @Test
    void testLoggingAlertDecorator() {
        Alert baseAlert = new BloodPressureAlert("2", "Low Blood Pressure", System.currentTimeMillis());

        LoggingAlertDecorator loggingDecorator = new LoggingAlertDecorator(baseAlert);

        assertEquals(baseAlert.getPatientId(), loggingDecorator.getPatientId());
        assertEquals(baseAlert.getCondition(), loggingDecorator.getCondition());
        assertEquals(baseAlert.getTimestamp(), loggingDecorator.getTimestamp());

        loggingDecorator.getCondition(); // This should log the alert details to the console
    }

    @Test
    void testPriorityAlertDecorator() {
        Alert baseAlert = new BloodPressureAlert("3", "Critical Blood Pressure", System.currentTimeMillis());

        PriorityAlertDecorator priorityDecorator = new PriorityAlertDecorator(baseAlert, 1);

        assertEquals("[Priority: 1]Critical Blood Pressure", priorityDecorator.getCondition());

        priorityDecorator.setPriority(2);
        assertEquals("[Priority: 2]Critical Blood Pressure", priorityDecorator.getCondition());
    }
}