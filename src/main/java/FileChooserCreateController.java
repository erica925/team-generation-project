import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FileChooserCreateController implements Initializable {
    private List<File> newStudentsChosenFiles;
    private List<String> newStudentsChosenFileNames;
    @FXML
    private TextArea updatesText;
    @FXML
    private Button insertNewStudentsButton;

    @Override
    /**
     * Connects Model to Controller
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Handles the event when a user clicks the new students button
     * @param e The event
     */
    public void insertNewStudentsFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser(); // creates a file chooser to choose a file
        File defaultDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(defaultDirectory);

        // sets a filter to allow *.csv files only
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        newStudentsChosenFiles = fileChooser.showOpenMultipleDialog(insertNewStudentsButton.getScene().getWindow());

        newStudentsChosenFileNames = new ArrayList<>();
        for (File file : newStudentsChosenFiles) {
            newStudentsChosenFileNames.add(file.getPath());
        }
        updatesText.setText("New students file(s) selected: " + newStudentsChosenFileNames);
    }

    /**
     * Handles the event when the user clicks the sort students button
     * @param e The event
     * @throws IOException
     */
    public void createGroups(ActionEvent e) throws IOException {
        if(newStudentsChosenFiles == null) {
            GUIMain.noFileSelected(); // pop warning message displays to user
            return;
        }
        //submitCriteria();

        boolean insertSuccess = Main.readCSVCreate(newStudentsChosenFileNames); // reads the CSV file
        if(!insertSuccess){
            return;
        }

        Main.beginCreate();
        //Stage stage = (Stage) createButton.getScene().getWindow();
        updatesText.setText("Groups created, you may now close this window.");
        //stage.close();
    }
}
