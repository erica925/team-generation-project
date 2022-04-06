import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    /*@FXML
    private Button createCaseButton;
    @FXML
    private Button modifyCaseButton;
    @FXML
    private Spinner idealGroupSizeSpinner;
    @FXML
    private Button modifyButton;*/

    @Override
    /**
     * Connects Model to Controller
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Opens the window for choosing the optimization criteria to be used
     * @param e
     */
    public void openOptimizationCriteria(ActionEvent e) {
        GUIMain.startStage.close();
        GUIMain.optimizationCriteriaStage.show();
        //((Stage) createCaseButton.getScene().getWindow()).close();
        //((Stage) idealGroupSizeSpinner.getScene().getWindow()).show();
    }

    /**
     * Opens the window for choosing the input file(s) to be used for the modify groups use case
     * @param e
     */
    public void openFileChooserModify(ActionEvent e) {
        GUIMain.startStage.close();
        GUIMain.fileChooserModifyStage.show();
        //((Stage) modifyCaseButton.getScene().getWindow()).close();
        //((Stage) modifyButton.getScene().getWindow()).show();
    }
}
