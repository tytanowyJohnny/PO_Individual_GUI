package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class mathResultController {

    @FXML
    private Label addLabel;

    @FXML
    private Label minusLabel;

    @FXML
    private Label multiplyLabel;

    @FXML
    private Label substractLabel;

    @FXML
    private Button buttonOK;


    public void populateLabels(int addValue, int minusValue, int multipluValue, double substractValue) {

        addLabel.setText(String.valueOf(addValue));
        minusLabel.setText(String.valueOf(minusValue));
        multiplyLabel.setText(String.valueOf(multipluValue));
        substractLabel.setText(String.valueOf(substractValue));

    }

    @FXML
    private void handleOkButton(ActionEvent actionEvent) {

        Stage mainStage = (Stage) buttonOK.getScene().getWindow();
        mainStage.close();

    }

}
