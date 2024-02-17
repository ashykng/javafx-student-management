package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class reportController implements Initializable {
    @FXML private TextField stdCode, stdName, nameField, termField, entranceField, gpa;
    @FXML private TableView<myLessons> myTable;
    @FXML private TableColumn<myLessons, Integer> row;
    @FXML private TableColumn<myLessons, String > lesson, unit, classTime, teacher, examTime, score, status;
    @FXML private ComboBox<String> termBox;
    
    public String term = "14022";
    public static String entrance;
    private final ArrayList<String> capacity = new ArrayList<>() ;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String signEntr = signupController.entranceDate;
        String loginEntr = LoginController.entrance;
        
        if (signEntr!=null)
            entrance = signEntr;
        else
            entrance = loginEntr;
        
        entranceField.setText(entrance);
        
        String term = "14022";
        
        row.setCellValueFactory(new PropertyValueFactory<>("Row"));
        lesson.setCellValueFactory(new PropertyValueFactory<>("Lesson"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("Teacher"));
        classTime.setCellValueFactory(new PropertyValueFactory<>("ClassTime"));
        unit.setCellValueFactory(new PropertyValueFactory<>("Unit"));
        examTime.setCellValueFactory(new PropertyValueFactory<>("ExamTime"));
        score.setCellValueFactory(new PropertyValueFactory<>("Score"));
        status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        
        myTable.getItems().clear();
        
        stdName.setText(studentController.username);
        nameField.setText(studentController.username);
        stdCode.setText(studentController.studentCode);
        
        showLessons(myTable, studentController.username, term);
        
        handleTerms(capacity,entrance);
        
    }
    
    public void showLessons(TableView<myLessons> table, String username, String term){
        table.getItems().clear();
        try(BufferedReader stReader= new BufferedReader(new FileReader("StudentsFiles/"+term+"Students.txt"))) {
            float sum=0;
            int count=1;
            String stLine, lessonId;
            
            while ((stLine=stReader.readLine())!=null){
                String[] stParts = stLine.split(", ");
                
                if(stParts[0].equals(username)){
                    String score = stParts[2];
                    lessonId = stParts[3];
                    boolean flag = true;
                    try(BufferedReader lesReader= new BufferedReader(new FileReader("LessonsFiles/"+term+"Lessons.txt"))){
                        String lesLine;
                        while ((lesLine=lesReader.readLine())!=null && flag){
                            
                            if(lesLine.split(", ")[6].equals(lessonId)){
                                String[] lesParts = lesLine.split(", ");
                                String status = "Unknown" ;
                                
                                if(!score.equals("null")){
                                    sum+=Float.parseFloat(score);
                                    if(Float.parseFloat(score)>10)
                                        status = "Passed";
                                    else
                                        status = "Failed";
                                } else
                                    score ="";
                                
                                table.getItems().add(new myLessons(count++, lesParts[0], lesParts[1], lesParts[2], lesParts[5], lesParts[7], score, status));
                                flag = false;
                                
                            }
                        }
                    }catch (IOException e){
                        function.AddLog(username, e.getMessage());
                    }

                }
            }
            //writing gpa
            if(count!=0){
                DecimalFormat df = new DecimalFormat("#.##");
                gpa.setText(df.format(sum/count));
            }else
                gpa.setText("0");
            
        }catch (IOException e){
            function.AddLog(username, e.getMessage());
        }
    }
    
    public void changeTerm(){
        String item = termBox.getValue();
        termField.setText(item);
        term=termBox.getValue();
        showLessons(myTable, studentController.username, term);
    }
    
    public void handleTerms(ArrayList<String> array, String entrance){
        String[] entranceArr = entrance.split("-");
        int year = Integer.parseInt(entranceArr[0]);
        
        for(int i= 1402; i>year; i--){
            array.add(i+"2");
            array.add(i+"1");
        }
        //for handling BAHMAN entrances
        if(entranceArr[1].equals("1")){
            array.add(year+"2");
            array.add(year+"1");
        }else
            array.add(year+"2");
        
        termBox.getItems().addAll(capacity);
    }
    
    public void exit(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource( "login.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
        function.AddLog(studentController.username, "Logout");
        
    }
    public void back(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource( "student.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    
    public static class myLessons{
        String Lesson, Teacher, Unit, ClassTime, ExamTime, Score, Status;
        int Row;
        
        public myLessons(int row, String lesson, String teacher, String unit, String classTime, String examTime, String score, String status) {
            Lesson = lesson;
            Teacher = teacher;
            Unit = unit;
            ClassTime = classTime;
            ExamTime = examTime;
            Score = score;
            Status = status;
            Row=row;
        }
        
        public int getRow() {
            return Row;
        }
        public String getLesson() {
            return Lesson;
        }
        public String getTeacher() {
            return Teacher;
        }
        public String getUnit() {
            return Unit;
        }
        public String getClassTime() {
            return ClassTime;
        }
        public String getExamTime() {
            return ExamTime;
        }
        public String getScore() {
            return Score;
        }
        public String getStatus() {
            return Status;
        }
    }
}