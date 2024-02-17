package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class EditScoreController implements Initializable {
    
    @FXML private TextField nameField;
    @FXML private TextField LessonNameField;
    @FXML private TextField average;
    //table
    @FXML private TableView<Student> stTable;
    @FXML private TableColumn<Student, Integer> row;
    @FXML private TableColumn<Student, String> nameCol, stNumCol, scoreCol;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameField.setText(TeacherController.username);
        LessonNameField.setText(TeacherController.clicked_lesson_name);
        average.setText(TeacherController.averageScore);
        
        ShowFirstData();
    }
    
    
//<----------------------------------- METHODS & CLASSES -------------------------------------->
    public void ShowFirstData(){
        row.setCellValueFactory(new PropertyValueFactory<>("row"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        stNumCol.setCellValueFactory(new PropertyValueFactory<>("StNumber"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("ScoreField"));
        
        try(BufferedReader reader= new BufferedReader(new FileReader("StudentsFiles/14022Students.txt"))) {
            float sum=0;
            int count=0;
            String line;
            while ((line=reader.readLine())!=null){
                String[] parts = line.split(", ");
                if(parts[3].equals(TeacherController.clicked_lesson_id)) {
                    count++;
                    
                    if(!parts[2].equals("null"))
                        sum+=Float.parseFloat(parts[2]);
                    
                    stTable.getItems().add(new Student(count, parts[0], parts[1], parts[2]));
                }
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void AddScore(ActionEvent event) throws IOException {
        String LessonId = TeacherController.clicked_lesson_id;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Students_temp.txt")); BufferedReader br = new BufferedReader(new FileReader("StudentsFiles/14022Students.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");
                boolean flag = true;
                
                for (Student student : stTable.getItems()) {
                    if (student.getName().equals(data[0]) && LessonId.equals(data[3])) {
                        bw.write(data[0] + ", " + data[1] + ", " + student.getScoreFieldValue() + ", " + data[3]);
                        bw.newLine();
                        flag = false;
                    }
                }
                
                if(flag){
                    bw.write(line);
                    bw.newLine();
                }
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        //update Students.txt
        try {
            Files.move(Paths.get("Students_temp.txt"), Paths.get("StudentsFiles/14022Students.txt"), StandardCopyOption.REPLACE_EXISTING);
            function.AddLog(TeacherController.username,"'Students.txt' Updated");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        function.AddLog(TeacherController.username, "edit score for LessonId '"+LessonId+"' ");
        
        //go back to panel
        Parent loader = FXMLLoader.load(getClass().getResource("teacher.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    
    public void BackToPanel(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("teacher.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    public static class Student {
        String Name, StNumber, StScore;
        int Row;
        TextField ScoreField;
        
        public int getRow() {
            return this.Row;
        }
        public String getName() {
            return this.Name;
        }
        public String getStNumber() {
            return this.StNumber;
        }
        public String getStScore() {
            return this.StScore;
        }
        public TextField getScoreField(){
            return this.ScoreField;
        }
        public String getScoreFieldValue(){
            return this.ScoreField.getText();
        }
        
        public Student(int row, String name, String stNumber, String stScore) {
            Name = name;
            StNumber = stNumber;
            StScore = stScore;
            Row=row;
            ScoreField= new TextField(stScore);
            ScoreField.setStyle("-fx-background-color: rgb(250,255,250); -fx-background-radius: 6;" +
                                "-fx-border-color: #C8F2C8; -fx-border-radius: 5;");
        }
    }
    
}
