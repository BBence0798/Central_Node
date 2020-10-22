/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.central_node.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


/*
 * Központi egység ami vezérli az aktuátorokat és sensorokat
 * */
public class Central_Node extends Service {
    private ServerSocket serverSocket;  //ServerSocket, azonosítja a servert
    public static ObservableList<Socket> connectedSockets = FXCollections.observableArrayList(); //Ebben a listában tároljuk el a csatlakozott socketeket
    private Socket sc;
    private int port;
    
    /*
     * Működés elindítása új threaden, hogy ne blokkoljuk a UI-t.
     * */
    @Override
    protected Task createTask() {
        
        return new Task<Void>() {           
                @Override
                protected Void call() throws Exception {
                    serverSocket = new ServerSocket(port);
                    System.out.println(port);
                    while(true){
                        /*
                         * Várakozás szenzor kapcsolódására majd kommunikáció elindítása egy új thread-en
                         * */
                        sc=serverSocket.accept();
                        connectedSockets.add(sc);
                        new SensorHandler(sc).start();
                    }
                } 
        };     
    }
    
    /*
     * Működés leállítása
     * */
    public void stop(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    
}
        
