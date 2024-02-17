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
import java.util.ResourceBundle;

public class adminLessonController implements Initializable {
    public static String clicked_lesson_id;
    @FXML public TableView<Lesson> table;
    @FXML private TableColumn<Lesson, String> lesson, unit, teacher, classTime, examTime, countSt, id;
    @FXML private TableColumn<Lesson, Integer> row;
    @FXML private TableColumn<Lesson, Button> action;
    
    @FXML private TextField nameField;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameField.setText(adminController.username);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        lesson.setCellValueFactory(new PropertyValueFactory<>("Lesson"));
        unit.setCellValueFactory(new PropertyValueFactory<>("Unit"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("Teacher"));
        classTime.setCellValueFactory(new PropertyValueFactory<>("ClassTime"));
        countSt.setCellValueFactory(new PropertyValueFactory<>("CountSt"));
        examTime.setCellValueFactory(new PropertyValueFactory<>("ExamTime"));
        row.setCellValueFactory(new PropertyValueFactory<>("Row"));
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        action.setCellValueFactory(new PropertyValueFactory<>("Del"));
        
        showData(table);
    }
    
    public void showData(TableView<Lesson> table){
        table.getItems().clear();
        String[] date = function.getTerm().split("-");
        try(BufferedReader reader= new BufferedReader(new FileReader("LessonsFiles/"+date[0]+date[1]+"Lessons.txt"))) {
            int count=1;
            String line;
            while ((line=reader.readLine())!=null){
                String[] parts = line.split(", ");
                table.getItems().add(new Lesson(count++, parts[0], parts[1], parts[2], parts[3], parts[5], parts[7], parts[6]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    
    }
    
    
    EventHandler<MouseEvent> tableClicked = mouseEvent -> {
        if (mouseEvent.getClickCount() == 1 && !table.getSelectionModel().isEmpty()) {
            Lesson rowData = table.getSelectionModel().getSelectedItem();
            deleteLesson(rowData.getLesson(), rowData.getTeacher());
            table.getItems().remove(rowData);
            function.AddLog(adminController.username, "lesson '"+rowData.getLesson()+"' deleted");
        }

    };
    
    
    public static void deleteLesson(String lesson, String teacher) {
        //Edit 'Lessons.txt'
        try (BufferedReader reader = new BufferedReader(new FileReader("LessonsFiles/"+function.getTerm()+"Lessons.txt")); BufferedWriter writer = new BufferedWriter(new FileWriter("TempLessons.txt"))) {
            String lineToRemove = lesson + ", " + teacher;
            String currentLine;
            
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.contains(lineToRemove)) {
                    writer.write(currentLine);
                    writer.newLine();
                }
            }
            function.AddLog(adminController.username, "Lesson '"+lesson+"' removed successfully");
            
        } catch (IOException error) {
            function.AddLog(adminController.username, error.getMessage());
        }
        try {Files.move(Paths.get("TempLessons.txt"), Paths.get("LessonsFiles/"+function.getTerm()+"Lessons.txt"), StandardCopyOption.REPLACE_EXISTING);}
        catch (IOException e) {e.printStackTrace();}
        
        //Edit 'Teacher.txt'
        try (BufferedReader reader = new BufferedReader(new FileReader("Teachers.txt")); BufferedWriter writer = new BufferedWriter(new FileWriter("TempTeachers.txt"))) {
            String lineToRemove = teacher+": ";
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(lineToRemove)) {
                    if(currentLine.contains(", "+lesson))
                        currentLine = currentLine.replace(", "+lesson, "");
                    else
                        currentLine = currentLine.replace(lesson, "");
                }
                writer.write(currentLine);
                writer.newLine();
            }
            
        }catch (IOException error) {
            function.AddLog(adminController.username, error.getMessage());
        }
        
        try {
            Files.move(Paths.get("TempTeachers.txt"), Paths.get("Teachers.txt"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
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
        String Lesson,Unit, Teacher, ClassTime, ExamTime, CountSt, Id;
        int Row;
        public Button Del;
        
        public Lesson(int row, String lesson, String teacher , String unit, String countSt, String classTime, String examTime, String id) {
            Lesson = lesson;
            Unit = unit;
            CountSt = countSt;
            Teacher = teacher;
            ClassTime = classTime;
            ExamTime = examTime;
            Row = row;
            Id = id;

            Del= new Button("Delete");
            Del.setOnMouseClicked(tableClicked);
            Del.setPrefWidth(50);
            Del.setPrefHeight(20);
            Del.setStyle("-fx-background-color: red ; -fx-cursor: hand;" +
                    " -fx-font-size: 10px; -fx-font-weight: bold;" +
                    "-fx-text-fill: white; -fx-padding-left: 5;");
        }
        
        public String getCountSt() {
            return CountSt;
        }
        public String getLesson() {
            return Lesson;
        }
        public String getUnit() {
            return Unit;
        }
        
        public String getTeacher() {
            return Teacher;
        }
        
        public String getClassTime() {
            return ClassTime;
        }
        
        public String getExamTime() {
            return ExamTime;
        }
        
        public int getRow() {
            return Row;
        }
        
        public String getId() {
            return Id;
        }
        
        public Button getDel() {
            return Del;
        }
        
    }
    
}