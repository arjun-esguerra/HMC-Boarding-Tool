module com.hmc.createhmcuser {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hmc.createhmcuser to javafx.fxml;
    exports com.hmc.createhmcuser;
}