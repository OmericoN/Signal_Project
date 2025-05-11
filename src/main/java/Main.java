import java.io.IOException;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("DataStorage")) {
            DataStorage.getInstance();
        } else {
            try{
                HealthDataSimulator.getInstance(args);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
