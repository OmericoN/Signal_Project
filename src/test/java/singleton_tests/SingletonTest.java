package singleton_tests;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

public class SingletonTest {

    @Test
    void testDataStorageSingleton() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertNotNull(instance1, "DataStorage instance should not be null");
        assertSame(instance1, instance2, "Both instances of DataStorage should be the same");
    }

    @Test
    void testHealthDataSimulatorSingleton() throws IOException {
        String[] args = {"--patient-count", "100"};
        HealthDataSimulator instance1 = HealthDataSimulator.getInstance(args);
        HealthDataSimulator instance2 = HealthDataSimulator.getInstance(args);

        assertNotNull(instance1, "HealthDataSimulator instance should not be null");
        assertSame(instance1, instance2, "Both instances of HealthDataSimulator should be the same");
    }
}