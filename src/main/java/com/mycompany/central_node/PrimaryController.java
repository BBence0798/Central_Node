package com.mycompany.central_node;

import com.mycompany.central_node.model.Central_Node;
import static com.mycompany.central_node.model.Central_Node.connectedSockets;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class PrimaryController implements Initializable{

    @FXML
    private Button InditasButton;
    
    @FXML
    private GridPane gridPane;
    
    //Egyszer fut le. A Scene indulásakor.
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        //Figyelem, h történik e változás a kapcsolódott socket-ek listájában. Listenert adok a listához.
        connectedSockets.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                System.out.println("\nonChanged()");

                while(c.next()){
                    System.out.println("next: ");
                    if(c.wasAdded()){
                        System.out.println("- wasAdded");
                    }
                    if(c.wasPermutated()){
                        System.out.println("- wasPermutated");
                    }
                    if(c.wasRemoved()){
                        System.out.println("- wasRemoved");
                    }
                    if(c.wasReplaced()){
                        System.out.println("- wasReplaced");
                    }
                    if(c.wasUpdated()){
                        System.out.println("- wasUpdated");
                    }
                }
                
                for(Object i : connectedSockets){
                    System.out.println(i);
                }
                //Itt adnám hozzá az FXML-hez a listview-t dinamikusan (ha új kapcsolat van) Még nem mükszik.
                ListView lw=new ListView();
                gridPane.add(lw, 0, 0);
                
            }         
        });
    }
        

    //Indítás gomb funkcionalítása
    @FXML
    public void runAction(){
        Central_Node central_node = new Central_Node();
        central_node.start(3000);
    }
    
}
