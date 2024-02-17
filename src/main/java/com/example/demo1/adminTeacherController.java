package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class adminTeacherController implements Initializable {
    
    @FXML private TableView<Lesson> table;
    @FXML private TableColumn<Lesson, Integer> row;
    @FXML private TableColumn<Lesson, String> teacher, lessons;
    @FXML private TableColumn<Lesson, Button> action;
    
    @FXML private TextField nameField;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.getItems().clear();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        row.setCellValueFactory(new PropertyValueFactory<>("Row"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("Teacher"));
        lessons.setCellValueFactory(new PropertyValueFactory<>("Lessons"));
        action.setCellValueFactory(new PropertyValueFactory<>("Del"));
        nameField.setText(adminController.username);
        handleData(table);
    }
    
    
    public void handleData(TableView table){
        try(BufferedReader br = new BufferedReader(new FileReader("Teachers.txt"))) {
            String line;
            int count =1 ;
            while ((line=br.readLine())!=null){
                String[] data = line.split(": ");
                table.getItems().add(new Lesson(count++, data[0], data[1]));
            }
            
        }catch (IOException e){
            function.AddLog(adminController.username, e.getMessage());
        }
        
    }
    
    EventHandler<MouseEvent> tableClicked = mouseEvent -> {
        if (mouseEvent.getClickCount() == 1 && !table.getSelectionModel().isEmpty()) {
            Lesson rowData = table.getSelectionModel().getSelectedItem();
            delTeacher(rowData.getTeacher());
            table.getItems().remove(rowData);
            function.AddLog(adminController.username, "Teacher '"+rowData.getTeacher()+"' deleted");
        }
    };
    
    public static void delTeacher(String name) {
        //remove teacher from 'Teachers.txt'
        ArrayList<String> lessonsToDel = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("Teachers.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("tempTeachers.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (!line.contains(name)) {
                    writer.write(line);
                    writer.newLine();
                } else{
                    
                    Collections.addAll(lessonsToDel, line.split(": ")[1].split(", "));
                }
                
            }
            
        } catch (IOException e) {
            function.AddLog(adminController.username, e.getMessage());
        }
        
        try {
            Files.move(Paths.get("tempTeachers.txt"), Paths.get("Teachers.txt"), REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        //remove lessons from 'Lessons.txt'
        File lessonFile = new File("LessonsFiles/"+function.getTerm()+"Lessons.txt");
        File tmpLessonFile = new File("tempLessons.txt");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(lessonFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tmpLessonFile))) {
            
            String line;
            while ((line=reader.readLine())!=null){
                boolean flag = true;
                
                for (String lessonToDel : lessonsToDel) {
                    if (line.split(", ")[1].equals(name) && line.split(", ")[0].equals(lessonToDel)) {
                        flag = false;
                        break;
                    }
                    
                }
                
                if(flag){
                    writer.write(line);
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();
            
            try {
                Files.move(Paths.get("tempLessons.txt"), Paths.get("LessonsFiles/"+function.getTerm()+"Lessons.txt"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } catch (IOException e) {
            function.AddLog(adminController.username, e.getMessage());
        }
        
    }
    
    @FXML void GoAddLesson(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("adminAddLesson.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    @FXML void GoProfList(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("adminTeacher.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    @FXML void exit(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
        function.AddLog(adminController.username, "Logout");
    }
    
    @FXML void goHome(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("admin.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
    
    @FXML void goLessonList(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource( "adminLesson.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }

    
    public class Lesson{
        String Teacher, Lessons;
        int Row;
        Button Del;
        
        public Lesson(int row, String teacher, String lessons) {
            Teacher = teacher;
            Lessons = lessons;
            Row = row;
            
            Del= new Button("Delete");
            Del.setOnMouseClicked(tableClicked);
            Del.setPrefWidth(50);
            Del.setPrefHeight(20);
            Del.setStyle("-fx-background-color: red ; -fx-cursor: hand;" +
                    " -fx-font-size: 10px; -fx-font-weight: bold;" +
                    "-fx-text-fill: white; -fx-padding-left: 5;");
        }
        
        public String getTeacher() {
            return Teacher;
        }
        public String getLessons() {
            return Lessons;
        }
        public int getRow() {
            return Row;
        }
        public Button getDel() {
            return Del;
        }
        
    }
}