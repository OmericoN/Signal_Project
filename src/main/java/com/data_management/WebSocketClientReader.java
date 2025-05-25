package com.data_management;

import java.io.IOException;
import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


public class WebSocketClientReader extends WebSocketClient implements DataReader{
    private DataStorage dataStorage;

    public WebSocketClientReader (URI serverUri){
        super(serverUri);
    }
    @Override
    public void startStream(DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;
        this.connect();
        
    }
    @Override
    public void stopStream(){
        this.close();
    }

    @Override
    public void onOpen(ServerHandshake handshake){
        System.out.println("Websocket port connected");
    }
    @Override
    public void onClose(int closureCode, String reason, boolean remote){
        System.out.println("WebSocket port closed: " + reason);
    }
    @Override
    public void onError(Exception e){
        System.err.println("WebSocket error/exception: " + e.getMessage());
    }

    /**
     * Parses data from websocket port and stores in the DataStorage
     * @param message current stream message from the connected websocket port
     */
    @Override
    public void onMessage(String message){
        try {
            String[] parts = message.split(","); //I assume that the message uses the "," delimeter because of the WebSocket Strategy          
            if(parts.length != 4){
                System.out.println("Invalid message: " + message);
                return;
            }
            int patientId = Integer.parseInt(parts[0].strip());
            long timestamp = Long.parseLong(parts[1].strip());
            String recordType = parts[2].strip();
            double measurementVal = Double.parseDouble(parts[3].strip());

            dataStorage.addPatientData(patientId, measurementVal, recordType, timestamp);

        } catch (Exception e) {
            System.out.println("Failed to parse message");
            e.printStackTrace();
        }
    }


    
}
