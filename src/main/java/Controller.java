import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


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
    private Button submitButton;
    @FXML
    private Button insertButton;
    @FXML
    private Spinner<Integer> maxGroupSizeSpinner;

    private File chosenFile;
    private Main model;


    @Override
    /**
     * Initializes maximum group size spinner and connects Model to Controller
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new Main();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10);
        valueFactory.setValue(2); // initial value of spinner
        maxGroupSizeSpinner.setValueFactory(valueFactory);

    }


    /**
     * Handles the event when a user click's the insert file button
     * @param e The event
     */
    public void insertFile(ActionEvent e){
        FileChooser fileChooser = new FileChooser(); // creates a file chooser to choose a file

        // sets a filter to allow *.csv files only
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        chosenFile = fileChooser.showOpenDialog(insertButton.getScene().getWindow());

    }

    /**
     * Handles the event when the user clicks the submit criteria button
     * @param e The event
     * @throws IOException
     */
    public void submitCriteria(ActionEvent e) throws IOException {

        boolean labSection = false;
        boolean grades = false;
        boolean programs = false;
        boolean teamLeader = false;


        if(chosenFile == null) {
            GUIMain.noFileSelected(); // pop warning message displays to user
            return;
        }

        if(checkbox1.isSelected()){

            labSection = true;
            model.setLabSectionFlag(true); // sets lab section flag in model

        }

        if(checkbox2.isSelected()){

            grades = true;
            model.setGradeFlag(true); // sets grade flag in model

        }

        if(checkbox3.isSelected()){

            programs = true;
            model.setProgramsFlag(true); // sets program flag in model

        }

        if(checkbox4.isSelected()){

            teamLeader = true;
            model.setTeamLeaderFlag(true); // sets team leader flag in model

        }

        int maxGroupSize = maxGroupSizeSpinner.getValue(); // gets the group size from spinner
        model.setMaximumGroupSize(maxGroupSize); // sets the size in model
        Main.readCSV(chosenFile.getName()); // reads the CSV file

    }



}
