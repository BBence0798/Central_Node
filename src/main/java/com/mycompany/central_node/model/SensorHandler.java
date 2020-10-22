/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.central_node.model;

import static com.mycompany.central_node.PrimaryController.Messages;
import static com.mycompany.central_node.model.Central_Node.connectedSockets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/*
 * Kezeli a szenzorokat, egy új thread-ben, minden szenzorral való kommunikáció új thread-ben valósul meg
 * */
public class SensorHandler extends Service{
    private Socket sensorSocket;   //Sensor/client socket
    private PrintWriter out;  //output - kimenet a kliens felé
    private BufferedReader in; //input  - bemenet a kliens felől
    private String uzenet;
    
    
    public SensorHandler(Socket sensorSocket) {
        this.sensorSocket = sensorSocket;
    }

    @Override
    protected Task createTask() {
        
        return new Task<Void>() {           
                @Override
                protected Void call() throws Exception {
                    out = new PrintWriter(sensorSocket.getOutputStream(),true);
                    in = new BufferedReader(new InputStreamReader(sensorSocket.getInputStream()));

                    //Folyamatosan figyejük a kapott üzenteket
                    while((uzenet=in.readLine())!=null){
                        if(uzenet.equals("VEGE")){

                            //A csatlakozott socketek listájától eltávolítom a socketet.
                            connectedSockets.remove(sensorSocket);

                            System.out.println("A Sensor lezárta a kapcsolatot");
                            Messages.add("Connection over");
                            break;
                        }else{
                            Messages.add(uzenet);
                            System.out.println("Kapott üzenet: "+uzenet);
                        }
                    }

                in.close();
                out.close();
                sensorSocket.close();
                return null; 
                }
            };
}
}
