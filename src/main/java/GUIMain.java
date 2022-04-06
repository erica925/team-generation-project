import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;


public class GUIMain extends Application {
    Parent startRoot;
    Parent fileChooserCreateRoot;
    Parent fileChooserModifyRoot;
    Parent optimizationCriteriaRoot;

    Scene startScene;
    Scene fileChooserCreateScene;
    Scene fileChooserModifyScene;
    Scene optimizationCriteriaScene;

    static Stage startStage;
    static Stage fileChooserCreateStage;
    static Stage fileChooserModifyStage;
    static Stage optimizationCriteriaStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("main/resources/GUI.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("main/resources/GUI2.fxml"));
        //Scene scene = new Scene(root);
        //stage.setScene(scene);
        //stage.setTitle("Team Generation Program");
        //stage.show();

        //URL url = new File("src/main/resources/GUI2.fxml").toURI().toURL();
        //Parent root = FXMLLoader.load(url);
        //Scene scene = new Scene(root);

        startRoot = FXMLLoader.load(getClass().getResource("main/resources/start.fxml"));
        fileChooserCreateRoot = FXMLLoader.load(getClass().getResource("main/resources/fileChooserCreate.fxml"));
        fileChooserModifyRoot = FXMLLoader.load(getClass().getResource("main/resources/fileChooserModify.fxml"));
        optimizationCriteriaRoot = FXMLLoader.load(getClass().getResource("main/resources/optimizationCriteria.fxml"));

        startScene = new Scene(startRoot);
        fileChooserCreateScene = new Scene(fileChooserCreateRoot);
        fileChooserModifyScene = new Scene(fileChooserModifyRoot);
        optimizationCriteriaScene = new Scene(optimizationCriteriaRoot);

        startStage = new Stage();
        fileChooserCreateStage = new Stage();
        fileChooserModifyStage = new Stage();
        optimizationCriteriaStage = new Stage();

        startStage.setScene(startScene);
        fileChooserCreateStage.setScene(fileChooserCreateScene);
        fileChooserModifyStage.setScene(fileChooserModifyScene);
        optimizationCriteriaStage.setScene(optimizationCriteriaScene);

        startStage.setTitle("Team Generation Program");
        fileChooserCreateStage.setTitle("Team Generation Program");
        fileChooserModifyStage.setTitle("Team Generation Program");
        optimizationCriteriaStage.setTitle("Team Generation Program");

        startStage.show();
        //fileChooserCreateStage.show();
        //fileChooserModifyStage.show();
        //optimizationCriteriaStage.show();
    }

    /**
     * Displays a message to user stating that the headers
     * in the CSV file are invalid.
     */
    public static void invalidFileHeaders(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Invalid header name, must contain " + message);
        alert.show();
    }

    /**
     * Displays a warning message to user stating that no
     * CSV file was selected.
     */
    public static void noFileSelected() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("No file selected");
        alert.show();
    }

    /**
     * Displays a warning message to user stating that
     * there is some information missing
     */
    public static void infoMissing() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Information missing from one of the files");
        alert.show();
    }

    /**
     * Displays an information message to user stating that
     * specific lab section groups are full and that
     * students could not be added to a group
     */
    public static void labSectionsFull(ArrayList<Student> withoutGroup){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String text = withoutGroup.size() + " new student(s) could not be added to a group\n\n";
        for(Student s: withoutGroup) {
            text += "Student Number: " + s.getStudID() + "\nName: " + s.getName() + "\nProgram: " + s.getProgram() +  "\nLab Section: " + s.getLabSection() + "\n\n";
        }
        alert.setContentText(text);
        alert.show();
    }
}
