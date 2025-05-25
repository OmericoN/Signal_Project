package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    //void readData(DataStorage dataStorage) throws IOException; //This is no longer used since we now read from server streams


    /**
     * Reads data from specified websocket source and stores parsed data in the data storage
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error parsing data
     */
    void startStream(DataStorage dataStorage) throws IOException;

    // This method closes the WebSocket connection/link to prevent potential data leaks 
    void stopStream();
}
