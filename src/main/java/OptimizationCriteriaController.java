import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class OptimizationCriteriaController implements Initializable {
    @FXML
    private CheckBox checkbox1;
    @FXML
    private CheckBox checkbox2;
    @FXML
    private CheckBox checkbox3;
    @FXML
    private CheckBox checkbox4;
    @FXML
    private Spinner<Integer> idealGroupSizeSpinner;

    @Override
    /**
     * Connects Model to Controller
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 9);
        valueFactory.setValue(2); // initial value of spinner
        idealGroupSizeSpinner.setValueFactory(valueFactory);
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
     * Opens the window for choosing the input file(s) to be used for the create groups use case
     * @param e
     */
    public void openFileChooserCreate(ActionEvent e) {
        GUIMain.optimizationCriteriaStage.close();
        GUIMain.fileChooserCreateStage.show();
        submitCriteria();
    }
}

