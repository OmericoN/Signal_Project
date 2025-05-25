package com.data_management;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileDataReader  { //no longer implements the DataReader since DataReader was adapted to WebSocket from week 5
// this would cause interface segragation otherwise
    private String path;
    public FileDataReader(String path){
        this.path = path;
    
    }

    //@Override no longer overrides DataReader since DataReader was adapted to WebSocket streaming instead
    public void readData(DataStorage dataStorage){
        double measurementValue; String recordType; long timestamp; //patientId isn't declared as int since it will be the leading read line to determine EOF
        try{
            BufferedReader reader = new BufferedReader(new FileReader(this.path)); //reads line by line from path
            String patientId = reader.readLine();
            while (patientId != null){
                measurementValue = Double.parseDouble(reader.readLine().trim());
                recordType = reader.readLine().trim();
                timestamp = Long.parseLong(reader.readLine().trim());
                if (recordType == null || patientId == null || patientId.trim().isEmpty()) {
                    System.out.println("Faulty information format");
                    continue; // Skip batch of patient data if not stored correctly in the file
                }
                dataStorage.addPatientData(Integer.parseInt(patientId), measurementValue, recordType, timestamp);
                patientId = reader.readLine(); //checks if next batch of patient exists in file otherwise EOF
            }

            reader.close();
        } catch (IOException e){
            System.err.println("Error reading the file, please verify file path");
            e.printStackTrace();
        }
    }


}
