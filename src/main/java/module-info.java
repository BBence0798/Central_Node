module com.mycompany.central_nod {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.central_node to javafx.fxml;
    exports com.mycompany.central_node;
}
