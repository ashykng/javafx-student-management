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
import java.util.ResourceBundle;

public class studentLessonsController implements Initializable {
    @FXML private TextField stdCode, stdName, nameField, loginTime;
    @FXML private TextField successMsg;
    @FXML public TableView<SelectedLessons> myTable;
    @FXML private TableColumn<SelectedLessons, Integer> row;
    @FXML private TableColumn<SelectedLessons, Button> action;
    @FXML private TableColumn<SelectedLessons, String> lesson, unit, teacher, classInfo, classTime, examDate;
    public static ArrayList<String> selectedLessons = studentController.selectedLessons;
    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        showLessons(myTable);
        myTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        row.setCellValueFactory(new PropertyValueFactory<>("Row"));
        lesson.setCellValueFactory(new PropertyValueFactory<>("Lesson"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("Teacher"));
        classTime.setCellValueFactory(new PropertyValueFactory<>("ClassTime"));
        unit.setCellValueFactory(new PropertyValueFactory<>("Unit"));
        examDate.setCellValueFactory(new PropertyValueFactory<>("ExamTime"));
        classInfo.setCellValueFactory(new PropertyValueFactory<>("ClassInfo"));
        action.setCellValueFactory(new PropertyValueFactory<>("Del"));
        
        stdName.setText(studentController.username);
        stdCode.setText(studentController.studentCode);
        nameField.setText(studentController.username);
        loginTime.setText(LoginController.time);
    }
    
    public EventHandler<MouseEvent> tableClicked = mouseEvent -> {
        if (mouseEvent.getClickCount() == 1 && !myTable.getSelectionModel().isEmpty()) {
            SelectedLessons rowData = myTable.getSelectionModel().getSelectedItem();
            selectedLessons.remove(rowData.getRow()-1);
            myTable.getItems().remove(rowData);
        }
    };
    public void showLessons(TableView<SelectedLessons> table){
        int count=1;
        
        for (String selectedLesson : selectedLessons) {
            try (BufferedReader brLessons = new BufferedReader(new FileReader("LessonsFiles/14022"+"Lessons.txt"))) {
                String line;
                String[] lessonData;
                
                while ((line = brLessons.readLine()) != null) {
                    lessonData = line.split(", ");
                    
                    if (selectedLesson.equals(lessonData[6])) {
                        String[] data = line.split(", ");
                        table.getItems().add(new SelectedLessons(count++, data[0], data[1], data[2], data[5], data[7], data[8]));
                    }
                    
                }
                
            } catch (Exception e) {
                function.AddLog(studentController.username, e.getMessage());
            }
        }
    }
    

    
    public void chooseLessons(String studentName, String stNum, String lessonId){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("StudentsFiles/14022Students.txt",true));
            BufferedReader reader = new BufferedReader(new FileReader("LessonsFiles/14022"+"Lessons.txt")); BufferedWriter writer = new BufferedWriter(new FileWriter("lessons_temp.txt"))) {
            
            String line;
            while ((line = reader.readLine())!=null){
                String[] parts = line.split(", ");
                if (parts[6].equals(lessonId)) {
                    int newCount = Integer.parseInt(parts[3])+1;
                    String newLine = parts[0]+", "+parts[1]+", "+parts[2]+", "+newCount+", "+parts[4]+", "+parts[5]+", "+parts[6]+", "+parts[7]+", "+parts[8]+", "+parts[9];
                    writer.write(newLine);
                    writer.newLine();
                }else {
                    writer.write(line);
                    writer.newLine();
                }
            }
            bw.write(studentName + ", " + stNum + ", null, " + lessonId);
            bw.newLine();;
            
        } catch (Exception e) {
            function.AddLog(studentName, "error while writing new lesson"+e.getMessage());
        }
        
        try {
            Files.move(Paths.get("lessons_temp.txt"), Paths.get("LessonsFiles/14022"+"Lessons.txt"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void done(ActionEvent event) throws InterruptedException, IOException {
        
        for(String selectedLesson : selectedLessons) {
            chooseLessons(studentController.username, studentController.studentCode, selectedLesson);
        }
        function.AddLog(studentController.username,"success unit selection");
        successMsg.setText("success unit selection! you're gonna go back to Login page in 3sec ");
        System.out.println("success unit selection! you're gonna go back to Login page in 3sec ");
        Thread.sleep(3000);
        
        Parent loader = FXMLLoader.load(getClass().getResource( "login.fxml"));
        Scene scene = new Scene(loader);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
        function.AddLog(studentController.username, "Logout");
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
    
    
    public class SelectedLessons{
        String Lesson, Teacher, Unit, ClassTime, ClassInfo, ExamTime;
        int Row;
        Button Del;
        
        public SelectedLessons(int row, String lesson, String teacher, String unit, String classTime, String examTime, String classInfo ) {
            Lesson = lesson;
            Teacher = teacher;
            Unit = unit;
            ClassTime = classTime;
            ClassInfo = classInfo;
            ExamTime = examTime;
            Row = row;
            
            Del= new Button("Delete");
            Del.setOnMouseClicked(tableClicked);
            Del.setPrefWidth(50);
            Del.setPrefHeight(20);
            Del.setStyle("-fx-background-color: red ; -fx-cursor: hand;" +
                    " -fx-font-size: 10px; -fx-font-weight: bold;" +
                    "-fx-text-fill: white; -fx-padding-left: 5;");
        }
        
        public Button getDel() {
            return Del;
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

        public String getClassInfo() {
            return ClassInfo;
        }

        public String getExamTime() {
            return ExamTime;
        }

        public int getRow() {
            return Row;
        }

    }
}