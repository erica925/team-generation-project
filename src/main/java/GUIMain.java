import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class GUIMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main/resources/GUI.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Team Generation Program");
        stage.show();
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
