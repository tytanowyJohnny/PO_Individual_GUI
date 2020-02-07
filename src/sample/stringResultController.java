package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class stringResultController {

    @FXML
    private Label replyTextLabel;

    @FXML
    private Button buttonOK;


    public void setReplyTextLabel(String text) {

        replyTextLabel.setText(text);
    }

    @FXML
    private void handleOkButton(ActionEvent actionEvent) {

        Stage mainStage = (Stage) buttonOK.getScene().getWindow();
        mainStage.close();
    }
}
