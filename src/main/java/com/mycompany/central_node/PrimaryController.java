package com.mycompany.central_node;

import com.mycompany.central_node.model.Central_Node;
import static com.mycompany.central_node.model.Central_Node.connectedSockets;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class PrimaryController implements Initializable{

    @FXML
    private Button InditasButton;
    
    @FXML
    private GridPane gridPane;
    
    private Boolean nulladikOszlop=true;
    private int paneElemenetDB=0;
    public static ObservableList<String> Messages = FXCollections.observableArrayList();
    
    //Egyszer fut le. A Scene indulásakor.
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        //Listákban tárolom a megjelenítendő UI elemeket.
        List<ListView> l = new ArrayList<>();
        List<Text> t = new ArrayList<>();
        
        /*Figyelem, h történik e változás a kapcsolódott socket-ek
        listájában. Listenert adok a listához.*/
        connectedSockets.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                while(c.next()){
                    System.out.println("next: ");
                    if(c.wasAdded()){
                        System.out.println("- wasAdded");
                        //Itt adom a Pane-hez dinamikusan a megfelelő UI elemeket a listából.
                        Platform.runLater(new Runnable() {                          
                        @Override
                        public void run() {
                            if(paneElemenetDB==0){
                            l.add(new ListView());
                            t.add(new Text(""));
                            gridPane.add(l.get(0), 0, 1);
                            gridPane.add(t.get(0), 0, 0);
                            nulladikOszlop=false;
                            paneElemenetDB+=2;
                            }else if(!nulladikOszlop){
                            l.add(new ListView());  
                            t.add(new Text(""));
                            gridPane.add(l.get(paneElemenetDB/2), 1, paneElemenetDB-1);
                            gridPane.add(t.get(paneElemenetDB/2), 1, paneElemenetDB-2);
                            paneElemenetDB+=2;
                            nulladikOszlop=true;
                            }else if(nulladikOszlop){
                            l.add(new ListView());
                            t.add(new Text(""));
                            gridPane.add(l.get(paneElemenetDB/2), 0, paneElemenetDB-1);
                            gridPane.add(t.get(paneElemenetDB/2), 0, paneElemenetDB-2);
                            paneElemenetDB+=2;
                            nulladikOszlop=false;
                            }
                        }
                    });
                    }
                }
                
            }         
        });
        
        //Még nem figyeljük, hogy kitől jött az üzenet. Ezt le kell cserélni egy jobb megoldásra.
        Messages.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                while(c.next()){
                    if(c.wasAdded()){
                        Platform.runLater(new Runnable() {                          
                        @Override
                        public void run() {
                            if(t.get((paneElemenetDB/2)-1).getText().equals("")){
                                //Beállítjuk a fejlécet a megfelelő Texthez.
                                String[] token;
                                if(((paneElemenetDB/2))-1==0){
                                   token=Messages.get(0).split(" ");
                                }else{
                                   //HARDCODED 
                                   token=Messages.get((((paneElemenetDB/2)-1)*10)+2).split(" ");
                                }
                                t.get((paneElemenetDB/2)-1).setText(token[1]+" ("+token[2]+","+token[3]+")");
                            }else{
                                //A megfeleő ListView-hoz hozzáadjuk a kapott üzenetet.
                                l.get((paneElemenetDB/2)-1).getItems().add(Messages.get(Messages.size()-1));
                            }
                        }
                    });
                    }
                }
            }
        });
         
    }
        

    //Indítás gomb funkcionalítása
    @FXML
    public void runAction(){
        Central_Node central_node = new Central_Node();
        central_node.setPort(3000);
        central_node.start();    
    }
    
}
