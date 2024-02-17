package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class adminAddLessonController implements Initializable {
    public static boolean flag = true;
    @FXML private TextField ClassInfo, ExamVenue, classTime, examDate, lesson, nameField, teacher, unit;
    @FXML private Label msg;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameField.setText(adminController.username);
    }
    
    public void add(){
        String lessonF = lesson.getText();
        String teacherF = teacher.getText();
        String classTimeF = classTime.getText();
        String examDateF = examDate.getText();
        String ExamVenueF = ExamVenue.getText();
        String ClassInfoF = ClassInfo.getText();
        String unitF = unit.getText();
        
        
        if(!(lessonF.isEmpty() || teacherF.isEmpty() || unitF.isEmpty() || classTimeF.isEmpty() || examDateF.isEmpty() || ExamVenueF.isEmpty() || ClassInfoF.isEmpty())){
            
            addLesson(lessonF, teacherF, unitF, classTimeF, getLessonId(), examDateF, ExamVenueF, ClassInfoF);
            
            if(flag){
                msg.setText("Lesson successfully added !");
                msg.setStyle("-fx-text-fill: green");
                lesson.setText("");
                teacher.setText("");
                //clasDate.setText("");
                classTime.setText("");
                examDate.setText("");
                ExamVenue.setText("");
                ClassInfo.setText("");
                unit.setText("");
                
            }else {
                msg.setText("Something went wrong (check the Log file) !");
                msg.setStyle("-fx-text-fill: red");
            }
        }else{
            msg.setText("Fill the form !");
            msg.setStyle("-fx-text-fill: red");
        }
    }
    
    public static int getLessonId(){
        // Auto increment
        int lastId=0;
        try (BufferedReader reader = new BufferedReader(new FileReader("LessonsFiles/"+function.getTerm()+"LessonsFiles"+function.getTerm()+"Lessons.txt"))) {
            String line;
            while ((line=reader.readLine())!=null){
                if(Integer.parseInt(line.split(", ")[6])>lastId)
                    lastId = Integer.parseInt(line.split(", ")[6]);
            }
            
        }catch (IOException e){
            function.AddLog(adminController.username, "admin get new lesson id - "+e.getMessage());
        }
        
        return (lastId+1);
    }
    
    public static void addLesson(String lesson, String teacher, String unit, String LessonTime, int LessonId, String examTime, String examVenue, String classInfo){
        //Edit 'Lessons.txt'
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("LessonsFiles"+function.getTerm()+"Lessons.txt", true))) {
            writer.write(lesson+", ");
            writer.write(teacher+", ");
            writer.write(unit+", ");
            writer.write("0"+ ", ");
            writer.write(function.getTerm()+", ");
            writer.write(LessonTime+", ");
            writer.write(LessonId+", ");
            writer.write(examTime+", ");
            writer.write(examVenue+", ");
            writer.write(classInfo);
            writer.newLine();

            function.AddLog(adminController.username, "New Lesson appended to the file successfully.");
            
        } catch (IOException error) {
            function.AddLog(adminController.username, "Error writing to the file for new Lesson: " + error.getMessage());
            flag=false;
        }
        
        //Edit 'Teachers.txt'
        try (BufferedReader reader = new BufferedReader(new FileReader("Teachers.txt")); BufferedWriter writer = new BufferedWriter(new FileWriter("TempTeachers.txt"))) {
            String lineToEdit = teacher+": ";
            String currentLine;
            boolean isTeacherExist = false;
            while ((currentLine = reader.readLine()) != null) {
                
                if (!currentLine.contains(lineToEdit)) {
                    writer.write(currentLine);
                    writer.newLine();
                    isTeacherExist = true;
                }
                else { //if teacher already exist just add lesson name
                    writer.write(currentLine+", "+lesson);
                    writer.newLine();
                    isTeacherExist = false;
                }
            }
            if(!isTeacherExist){ //if teacher does not exist add teacher and lesson
                writer.write(teacher+": "+lesson);
            }
            
        } catch (IOException error) {
            function.AddLog(adminController.username, "Error manipulating the file: " + error.getMessage());
            flag=false;
        }
        
        try {
            Files.move(Paths.get("TempTeachers.txt"), Paths.get("Teachers.txt"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            function.AddLog(adminController.username, e.getMessage());
            flag=false;
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
    
    

}