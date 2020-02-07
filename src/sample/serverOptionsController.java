package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;


import java.net.InetAddress;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class serverOptionsController implements Initializable {

    @FXML
    private Button mathButton;

    @FXML
    private Button stringButton;

    @FXML
    private Button exitButton;

    private ServerTCP_New server;
    private ClientTCP_Listener client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            // Start  Server
            server = new ServerTCP_New(4848);

            // Connect client
            client = new ClientTCP_Listener(InetAddress.getByName("localhost").toString().split("/")[1], 4848);


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @FXML
    private void handleMathButton(ActionEvent actionEvent) {

        int numOne = 0;
        int numTwo = 0;

        TextInputDialog dialogOne = new TextInputDialog("0");
        dialogOne.setTitle("Provide first number");
        dialogOne.setHeaderText("Please provide first of 2 numbers for calculation");
        dialogOne.setContentText("Number:");

        Optional<String> resultOne = dialogOne.showAndWait();

        if(resultOne.isPresent())
            numOne = Integer.parseInt(resultOne.get());

        TextInputDialog dialogTwo = new TextInputDialog("0");
        dialogTwo.setTitle("Provide first number");
        dialogTwo.setHeaderText("Please provide second of 2 numbers for calculation");
        dialogTwo.setContentText("Number:");

        Optional<String> resultTwo = dialogTwo.showAndWait();

        if(resultTwo.isPresent())
            numTwo = Integer.parseInt(resultTwo.get());


        client.send("math;" + numOne + ";" + numTwo);

    }

    @FXML
    private void handleStringButton(ActionEvent actionEvent) {

        String wordToReplace = "";
        String word = "";
        String text = "";

        TextInputDialog dialogOne = new TextInputDialog("");
        dialogOne.setTitle("Change words in text");
        dialogOne.setHeaderText("Provide text where would you like to make the changes");
        dialogOne.setContentText("Text:");

        Optional<String> resultOne = dialogOne.showAndWait();

        if(resultOne.isPresent())
            text = resultOne.get();

        TextInputDialog dialogTwo = new TextInputDialog("");
        dialogTwo.setTitle("Change words in text");
        dialogTwo.setHeaderText("What word would you like to replace?");
        dialogTwo.setContentText("Word:");

        Optional<String> resultTwo = dialogTwo.showAndWait();

        if(resultTwo.isPresent())
            wordToReplace = resultTwo.get();

        TextInputDialog dialogThree = new TextInputDialog("");
        dialogThree.setTitle("Change words in text");
        dialogThree.setHeaderText("What is the replacement word?");
        dialogThree.setContentText("Word:");

        Optional<String> resultThree = dialogThree.showAndWait();

        if(resultThree.isPresent())
            word = resultThree.get();

        client.send("string;" + text + ";" + wordToReplace + ";" + word);

    }

    @FXML
    private void handleExitButton(ActionEvent actionEvent) {

        Stage mainStage = (Stage) exitButton.getScene().getWindow();
        mainStage.close();
    }
}
