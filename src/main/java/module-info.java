module com.hmc.createhmcuser {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.hmc.boardingtool to javafx.fxml;
    exports com.hmc.boardingtool;
}