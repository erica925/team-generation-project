import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

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
}
