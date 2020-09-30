/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.central_node.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * Központi egység ami vezérli az aktuátorokat és sensorokat
 * */
public class Central_Node {
    private ServerSocket serverSocket;  //ServerSocket, azonosítja a servert
    public static ObservableList<Socket> connectedSockets = FXCollections.observableArrayList(); //Ebben a listában tároljuk el a csatlakozott socketeket
    
    /*
     * Működés elindítása
     * */
    public void start(int port){
        try {
            Socket sc;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
