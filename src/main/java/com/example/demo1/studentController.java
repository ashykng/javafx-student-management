package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class studentController implements Initializable {
    public ArrayList<String> lessons = new ArrayList<String>();
    public static ArrayList<String> selectedLessons = new ArrayList<>();
    public static String username;
    public static String studentCode;
    @FXML private TextField nameField, stdName, loginTimeField, stdCode;
    @FXML private TextField course1, course2, course3, course4, course5, course6, course7, course8, course9;
    @FXML private TextField professor1, professor2, professor3, professor4, professor5, professor6, professor7, professor8, professor9;
    @FXML private TextField unit1, unit2, unit3, unit4, unit5, unit6, unit7, unit8, unit9;
    @FXML private TextField classLoc1, classLoc2, classLoc3, classLoc4, classLoc5, classLoc6, classLoc7, classLoc8, classLoc9;
    @FXML private TextField examInfo1, examInfo2, examInfo3, examInfo4, examInfo5, examInfo6, examInfo7, examInfo8, examInfo9;
    @FXML private TextField examination1, examination2, examination3, examination4, examination5, examination6, examination7, examination8, examination9;
    @FXML private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9;
    
    private String[] LessonsId = new String[9];
    private TextField[] Course, Professor, Unit, ExamInfo, Examination, ClassLoc;
    private CheckBox[] CheckBox;
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        selectedLessons.clear();
        
        //get username
        String signName = signupController.name;
        String LoginName = LoginController.name;
        if (signName!=null) {
            nameField.setText(signName);
            stdName.setText(signName);
            username = signName;
        }
        else {
            nameField.setText(LoginName);
            stdName.setText(LoginName);
            username = LoginName;
        }
        
        stdCode();
        loginTimeField.setText(LoginController.time);
        
        //show data
        handleData();
        
    }
    
    public void handleData(){
        Course = new TextField[]{course1, course2, course3, course4, course5, course6, course7, course8, course9};
        Professor = new TextField[]{professor1, professor2, professor3, professor4, professor5, professor6, professor7, professor8, professor9};
        Unit = new TextField[]{unit1, unit2, unit3, unit4, unit5, unit6, unit7, unit8, unit9};
        ExamInfo = new TextField[]{examInfo1, examInfo2, examInfo3, examInfo4, examInfo5, examInfo6, examInfo7, examInfo8, examInfo9};
        Examination = new TextField[]{examination1, examination2, examination3, examination4, examination5, examination6, examination7, examination8, examination9};
        ClassLoc = new TextField[]{classLoc1, classLoc2, classLoc3, classLoc4, classLoc5, classLoc6, classLoc7, classLoc8, classLoc9};
        CheckBox = new CheckBox[]{checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9};
        
        
        try(BufferedReader br= new BufferedReader(new FileReader("LessonsFiles/14022"+"Lessons.txt"))){
            String line;
            
            for(int i = 0; i< Course.length && (line=br.readLine())!=null; i++){
                String[] parts = line.split(", ");
                Course[i].setText(parts[0]);
                Professor[i].setText(parts[1]);
                Unit[i].setText(parts[2]);
                ExamInfo[i].setText(parts[7]);
                Examination[i].setText(parts[8]);
                ClassLoc[i].setText(parts[9]);
                LessonsId[i]=parts[6];
                
            }
        }catch (IOException e){
            e.getMessage();
        }
    }
    public void stdCode() {
        try(BufferedReader br = new BufferedReader(new FileReader("Users.txt"))){
            String line;
            boolean flag = true;
            while (((line = br.readLine()) != null) && flag) {
                String[] data = line.split(", ");
                if(data[0].equals(username) && data[3].equals("STUDENT")){
                    stdCode.setText(studentCode=data[1]);
                    flag=false;
                }
            }
            
        } catch(IOException e){
            function.AddLog(username, "get student code -"+e.getMessage());
        }
    }
    

    public void confirm(ActionEvent event) throws IOException {
        for(int i =0; i< CheckBox.length; i++){
            if(CheckBox[i].isSelected())
                saveLesson(username,LessonsId[i]);
        }
        
        Parent loader = FXMLLoader.load(getClass().getResource( "studentLessons.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    public void saveLesson(String studentName, String lessonId) {
        try (BufferedReader br = new BufferedReader(new FileReader("StudentsFiles/14022Students.txt"))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");
                if (studentName.equals(data[0]))
                    lessons.add(data[3]);
            }
            if(!lessons.contains(lessonId))
                selectedLessons.add(lessonId);
            
        } catch (Exception e) {
            function.AddLog(username, "error while saving new lessons"+e.getMessage());
        }
        
    }
    
    public void back(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource( "login.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    public void exit(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource( "login.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    public void goReport(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource( "report.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
}
    