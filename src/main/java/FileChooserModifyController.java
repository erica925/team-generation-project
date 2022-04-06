import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FileChooserModifyController implements Initializable {
    @FXML
    private Button modifyButton;
    @FXML
    private Button insertNewStudentsButton;
    @FXML
    private Button insertWithdrawnStudentsButton;
    @FXML
    private Button insertGroupsButton;
    @FXML
    private TextArea updatesText;

    private List<File> newStudentsChosenFiles;
    private List<File> withdrawnStudentsChosenFiles;
    private List<File> groupsChosenFiles;

    private List<String> newStudentsChosenFileNames;
    private List<String> withdrawnStudentsChosenFileNames;
    private List<String> groupsChosenFileNames;

    @Override
    /**
     * Connects Model to Controller
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Handles the event when the user clicks the modify groups button
     * @param e The event
     * @throws IOException
     */
    public void modifyGroups(ActionEvent e) throws IOException {
        if(newStudentsChosenFiles == null || withdrawnStudentsChosenFiles == null || groupsChosenFiles == null) {
            GUIMain.noFileSelected(); // pop warning message displays to user
            return;
        }

        boolean insertSuccess = Main.readCSVModify(newStudentsChosenFileNames, withdrawnStudentsChosenFileNames, groupsChosenFileNames); // reads the CSV file
        if(!insertSuccess){
            return;
        }

        Main.beginModify();
        updatesText.setText("Groups modified, you may now close this window and check " + System.getProperty("user.dir") + " for the groups and optimization summary.");
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
     * Handles the event when a user clicks the withdrawn students button
     * @param e The event
     */
    public void insertWithdrawnStudentsFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser(); // creates a file chooser to choose a file
        File defaultDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(defaultDirectory);

        // sets a filter to allow *.csv files only
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        withdrawnStudentsChosenFiles = fileChooser.showOpenMultipleDialog(insertWithdrawnStudentsButton.getScene().getWindow());

        withdrawnStudentsChosenFileNames = new ArrayList<>();
        for (File file : withdrawnStudentsChosenFiles) {
            withdrawnStudentsChosenFileNames.add(file.getPath());
        }
        updatesText.setText("Withdrawn students file(s) selected: " + withdrawnStudentsChosenFileNames);
    }

    /**
     * Handles the event when a user clicks the groups button
     * @param e The event
     */
    public void insertGroupsFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser(); // creates a file chooser to choose a file
        File defaultDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(defaultDirectory);

        // sets a filter to allow *.csv files only
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        groupsChosenFiles = fileChooser.showOpenMultipleDialog(insertGroupsButton.getScene().getWindow());

        groupsChosenFileNames = new ArrayList<>();
        for (File file : groupsChosenFiles) {
            groupsChosenFileNames.add(file.getPath());
        }
        updatesText.setText("Group file(s) selected: " + groupsChosenFileNames);
    }
}
