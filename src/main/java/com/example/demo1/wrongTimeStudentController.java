package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class wrongTimeStudentController implements Initializable {
    
    @FXML private TextField nameField;
    
    public void exit(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource( "login.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
        function.AddLog(studentController.username, "Logout");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameField.setText(LoginController.name);
    }
}
