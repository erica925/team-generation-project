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

/**
 * Controller for communications between GUI and Main
 *
 * @author Wintana Yosief, Erica Oliver
 * @version March 1, 2022
 */
public class Controller implements Initializable {
    @FXML
    private CheckBox checkbox1;
    @FXML
    private CheckBox checkbox2;
    @FXML
    private CheckBox checkbox3;
    @FXML
    private CheckBox checkbox4;
    @FXML
    private Button createButton;
    @FXML
    private Button modifyButton;
    @FXML
    private Button insertNewStudentsButton;
    @FXML
    private Button insertWithdrawnStudentsButton;
    @FXML
    private Button insertGroupsButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button createCaseButton;
    @FXML
    private Button modifyCaseButton;
    @FXML
    private Spinner<Integer> idealGroupSizeSpinner;
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
     * Initializes maximum group size spinner and connects Model to Controller
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10);
        valueFactory.setValue(2); // initial value of spinner
        idealGroupSizeSpinner.setValueFactory(valueFactory);
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

    /**
     * Sets the criteria flags and the max and min group sizes
     */
    public void submitCriteria() {
        if(checkbox1.isSelected()){
            Main.setLabSectionFlag(true); // sets lab section flag in model
        }
        if(checkbox2.isSelected()){
            Main.setGradeFlag(true); // sets grade flag in model
        }
        if(checkbox3.isSelected()){
            Main.setProgramsFlag(true); // sets program flag in model
        }
        if(checkbox4.isSelected()){
            Main.setTeamLeaderFlag(true); // sets team leader flag in model
        }

        int idealGroupSize = idealGroupSizeSpinner.getValue(); // gets the group size from spinner
        Main.setMaximumGroupSize(idealGroupSize); // sets the size in model
        Main.setMinimumGroupSize(); // sets the minimum size in model
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
        submitCriteria();

        boolean insertSuccess = Main.readCSVCreate(newStudentsChosenFileNames); // reads the CSV file
        if(!insertSuccess){
            return;
        }

        Main.beginCreate();
        Stage stage = (Stage) createButton.getScene().getWindow();
        updatesText.setText("Groups created, you may now close this window.");
        //stage.close();
    }

    /**
     * Handles the event when the user clicks the modify groups button
     * @param e The event
     * @throws IOException
     */
    public void modifyGroups(ActionEvent e) throws IOException {

        Main.setModifyFlag(true);
        if(newStudentsChosenFiles == null || withdrawnStudentsChosenFiles == null || groupsChosenFiles == null) {
            GUIMain.noFileSelected(); // pop warning message displays to user
            return;
        }
        submitCriteria();

        boolean insertSuccess = Main.readCSVModify(newStudentsChosenFileNames, withdrawnStudentsChosenFileNames, groupsChosenFileNames); // reads the CSV file
        if(!insertSuccess){
            return;
        }

        Main.beginModify();
        Stage stage = (Stage) modifyButton.getScene().getWindow();
        updatesText.setText("Groups modified, you may now close this window.");
        //stage.close();
    }

    /**
     * Opens the window for choosing the optimization criteria to be used
     * @param e
     */
    public void openOptimizationCriteria(ActionEvent e) {
        ((Stage) createCaseButton.getScene().getWindow()).close();
        ((Stage) idealGroupSizeSpinner.getScene().getWindow()).show();
    }

    /**
     * Opens the window for choosing the input file(s) to be used for the create groups use case
     * @param e
     */
    public void openFileChooserCreate(ActionEvent e) {
        ((Stage) nextButton.getScene().getWindow()).close();
        ((Stage) createButton.getScene().getWindow()).show();
    }

    /**
     * Opens the window for choosing the input file(s) to be used for the modify groups use case
     * @param e
     */
    public void openFileChooserModify(ActionEvent e) {
        ((Stage) modifyCaseButton.getScene().getWindow()).close();
        ((Stage) modifyButton.getScene().getWindow()).show();
    }
}
