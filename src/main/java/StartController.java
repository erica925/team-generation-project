import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

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
    }

    /**
     * Opens the window for choosing the input file(s) to be used for the modify groups use case
     * @param e
     */
    public void openFileChooserModify(ActionEvent e) {
        GUIMain.startStage.close();
        GUIMain.fileChooserModifyStage.show();
    }
}
