package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
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
    private Spinner<Integer> idealGroupSizeSpinner;
    @FXML
    private Spinner<Integer> maxGroupSizeSpinner;
    @FXML
    private Spinner<Integer> minGroupSizeSpinner;

    int idealGroupSize;
    int maxGroupSize;
    int minGroupSize;



    public void insertFile(ActionEvent e){
        FileChooser fileChooser = new FileChooser();
        File chosenFile = fileChooser.showOpenDialog(insertButton.getScene().getWindow());

    }

    public void submitCriteria(ActionEvent e){
        if(checkbox1.isSelected()){
            System.out.println("Lab Section");
            checkbox1.setSelected(false);
        }

        if(checkbox2.isSelected()){
            System.out.println("Grades");
            checkbox2.setSelected(false);
        }

        if(checkbox3.isSelected()){
            System.out.println("Programs");
            checkbox3.setSelected(false);
        }

        if(checkbox4.isSelected()){
            System.out.println("Team Leader");
            checkbox4.setSelected(false);
        }

        idealGroupSize = idealGroupSizeSpinner.getValue();
        maxGroupSize = maxGroupSizeSpinner.getValue();
        minGroupSize = minGroupSizeSpinner.getValue();

        System.out.println("Ideal Group Size: " + idealGroupSize);
        System.out.println("Max Group Size: " + maxGroupSize);
        System.out.println("Min Group Size: " + minGroupSize);



    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);

        valueFactory1.setValue(1);
        valueFactory2.setValue(1);
        valueFactory3.setValue(1);

        idealGroupSizeSpinner.setValueFactory(valueFactory1);
        maxGroupSizeSpinner.setValueFactory(valueFactory2);
        minGroupSizeSpinner.setValueFactory(valueFactory3);

    }
}
