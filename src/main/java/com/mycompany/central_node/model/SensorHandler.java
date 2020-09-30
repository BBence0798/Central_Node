/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.central_node.model;

import static com.mycompany.central_node.model.Central_Node.connectedSockets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Platform;

/*
 * Kezeli a szenzorokat, egy új thread-ben, minden szenzorral való kommunikáció új thread-ben valósul meg
 * */
public class SensorHandler extends Thread{
    private Socket sensorSocket;   //Sensor/client socket
    private PrintWriter out;  //output - kimenet a kliens felé
    private BufferedReader in; //input  - bemenet a kliens felől

    public SensorHandler(Socket sensorSocket) {
        this.sensorSocket = sensorSocket;
    }

    @Override
    public void run(){
        try {
            out = new PrintWriter(sensorSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(sensorSocket.getInputStream()));
            String uzenet;
            
            //Folyamatosan figyejük a kapott üzenteket
            while((uzenet=in.readLine())!=null){
                if(uzenet.equals("VEGE")){
                        //Ezt kell használni a THREAD-ben hogy módosítani tudjuk a UI elemeket.
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //A csatlakozott socketek listájától eltávolítom a socketet.
                                connectedSockets.remove(sensorSocket);
                            }
                        });
                    
                    System.out.println("A Sensor lezárta a kapcsolatot");
                }else{
                     System.out.println("Kapott üzenet: "+uzenet);
                }
            }
            
            
           
            /*
             * TODO: ide kerül, hogy mit csináljon a szerver (pl adat feldolgozása)
             * */
            
            in.close();
            out.close();
            sensorSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
