package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public static String time;
    public static final String passSalt = "9aR#5@jE!bFz^0p*2LcW8";
    public static String name;
    public static String entrance;
    
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Label error;
    
    private String[] capacity = {"STUDENT","TEACHER","ADMIN"};
    
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(capacity);
        choiceBox.setOnAction(this::choiceBox);
        LoginTime();
    }
    public void choiceBox(ActionEvent event) {
        String capacity =(String) choiceBox.getValue();
    }
    
    public void button(ActionEvent event) throws IOException {
        name = usernameField.getText();
        String pass = passwordField.getText();
        String rol = choiceBox.getValue();
        
        if (!(name.isEmpty() || pass.isEmpty() || rol.isEmpty())){
            
            //check username and password
            if(signIn(name, pass, rol)){
                function.AddLog(name,"login");
                
                //switch to panel  //TODO
                if(rol.equals("STUDENT") && !function.isCorrectTime(entrance)){
                    Parent loader = FXMLLoader.load(getClass().getResource("wrongtimeStudent.fxml"));
                    Scene scene = new Scene(loader);
                    Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(scene);
                    app_stage.show();
                }else {
                    Parent loader = FXMLLoader.load(getClass().getResource(rol.toLowerCase() + ".fxml"));
                    Scene scene = new Scene(loader);
                    Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(scene);
                    app_stage.show();
                }
            }
            else{
                error.setText("Wrong information !");
            }
        }
        else {
            error.setText("Fill the form");
        }
        
    }
    
    public void goSignUp(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    public void goPassRecovery(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource( "passwordRecovery.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    public void LoginTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        time = dtf.format(now);
    }


//    <------------------------------------ Methods ----------------------------------------->

    public boolean signIn(String user, String pass, String accessibility) {
        pass += passSalt;
        String hashedPass = function.hashString(pass);
        boolean LoginFlag = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            
            while (((line = reader.readLine()) != null) && !LoginFlag) {
                String[] data = line.split(", ");
                if (data[0].equals(user) && data[2].equals(hashedPass) && data[3].equals(accessibility)) {
                    name = data[0];
                    LoginFlag = true;
                    
                    if(accessibility.equals("STUDENT"))
                        entrance = data[4];
                }
            }
            
        } catch (IOException e) {
            function.AddLog(name,"Error reading from the file: " + e.getMessage());
        }

        return LoginFlag;
    }
    

}