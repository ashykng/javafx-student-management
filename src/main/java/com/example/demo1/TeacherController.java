package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {
    public static String username, clicked_lesson_name, clicked_lesson_id, averageScore;
    
    @FXML private TitledPane TitledPane;
    @FXML public TextField nameField;
    @FXML public TextField LessonNameField;
    @FXML public TextField average;
    
    //details Table
    @FXML private TableView<Person> stTable;
    @FXML private TableColumn<Person, Integer> row;
    @FXML private TableColumn<Person, String> nameCol, stNumCol, scoreCol;
    
    //lessons table
    @FXML private TableView<Lesson> LessonsTable;
    @FXML private TableColumn<Lesson, String> LesName, LesUnit, Les_stCount, LesDate, LesTime;
    @FXML private TableColumn<Lesson, Button> btnAction;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //show username
        String signName = signupController.name;
        String LoginName = LoginController.name;
        if (signName!=null) {
            nameField.setText(signName);
            username = signName;
        }
        else {
            nameField.setText(LoginName);
            username = LoginName;
        }
        
        //Lessons list table
        LesName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        LesUnit.setCellValueFactory(new PropertyValueFactory<>("Unit"));
        Les_stCount.setCellValueFactory(new PropertyValueFactory<>("NumberOfSt"));
        LesDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        LesTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
        LessonsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //detail table
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        stNumCol.setCellValueFactory(new PropertyValueFactory<>("StNumber"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("StScore"));
        row.setCellValueFactory(new PropertyValueFactory<>("row"));
        btnAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
        stTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ShowLessons(LessonsTable, "LessonsFiles/14022Lessons.txt", username);
    }
    
    //handle info btn
    EventHandler<MouseEvent> tableClicked = mouseEvent -> {
        if (mouseEvent.getClickCount() == 1 && !LessonsTable.getSelectionModel().isEmpty()) {
            Lesson rowData = LessonsTable.getSelectionModel().getSelectedItem();
            clicked_lesson_name = rowData.getName();
            clicked_lesson_id = rowData.getId();
            ShowStudents(stTable, "StudentsFiles/14022Students.txt",clicked_lesson_id);
            TitledPane.setExpanded(false);
            LessonNameField.setText(clicked_lesson_name);
        }
    };
    
    public void ShowStudents(TableView table, String fileName, String lessonId){
        table.getItems().clear();
        try(BufferedReader reader= new BufferedReader(new FileReader(fileName))) {
            float sum=0;
            int count=1;
            String line;
            while ((line=reader.readLine())!=null){
                String[] parts = line.split(", ");
                if(parts[3].equals(lessonId)) {
                    
                    if(!parts[2].equals("null"))
                        sum+=Float.parseFloat(parts[2]);
                    
                    table.getItems().add(new Person(count++, parts[0], parts[1], parts[2]));
                }
                //show average score
                if(count!=0){
                    DecimalFormat df = new DecimalFormat("#.##");
                    average.setText(averageScore=df.format(sum/count));
                }else
                    average.setText("0");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void ShowLessons( TableView table, String fileName, String username){
        try(BufferedReader reader= new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line=reader.readLine())!=null){
                String[] parts = line.split(", ");
                if(parts[1].equals(username))
                    table.getItems().add(new Lesson(parts[0], parts[2], parts[3], parts[4], parts[5], parts[6]));
            }
            
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void exit(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
        function.AddLog(TeacherController.username, "Logout");
    }
    
    public void EditScore(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("editScore.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    
    public class Lesson{
        public String Name, Unit, NumberOfSt, Date, Time, Id;
        public Button btn;
        
        public Lesson(String name, String unit, String numberOfSt, String date, String time, String id) {
            Name = name;
            Unit = unit;
            NumberOfSt = numberOfSt;
            Date = date;
            Time = time;
            Id = id;
            
            btn= new Button("info");
            btn.setOnMouseClicked(tableClicked);
            btn.setPrefWidth(50);
            btn.setPrefHeight(20);
            btn.setStyle("-fx-background-color: #48877c ; -fx-cursor: hand;" +
                        " -fx-font-size: 10px; -fx-font-weight: bold;" +
                        "-fx-text-fill: white; -fx-padding-left: 5;");
        }
        
        public String getName() {
            return Name;
        }
        
        public String getUnit() {
            return Unit;
        }
        
        public String getNumberOfSt() {
            return NumberOfSt;
        }
        
        public String getDate() {
            return Date;
        }
        
        public String getTime() {
            return Time;
        }
        public Button getBtn() {
            return btn;
        }
        public String getId() {
            return Id;
        }
    }
    
    
    public static class Person {
        String Name, StNumber, StScore;
        int Row;
        
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
        
        public Person(int row, String name, String stNumber, String stScore) {
            this.Name = name;
            this.StNumber = stNumber;
            this.StScore = stScore;
            this.Row=row;
        }
    }
}