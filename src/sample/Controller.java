package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;


public class Controller {

    @FXML
    private Button readFromFileButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button serverButton;

    @FXML
    private Button sqlButton;

    private ObservableList<String> items = FXCollections.observableArrayList ();

    @FXML
    private ListView<String> fileListView;

    @FXML
    private void handleReadFromFileButton(ActionEvent actionEvent) {

        Stage mainStage = (Stage) readFromFileButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(mainStage);

        //System.out.println(selectedFile.getAbsolutePath());

        multiThread threads = new multiThread(4, selectedFile);

        threads.run();

        items.addAll(threads.getArray());

        for(String item : items)
            System.out.println(item);

//        try {
//
//            // Open a new one
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("readFileResult.fxml"));
//            AnchorPane pane = loader.load();
//
//            Scene scene = new Scene(pane);
//            Stage stage = new Stage();
//
//            stage.setTitle("Result from reading file");
//            stage.setScene(scene);
//            stage.show();
//
//            fileListView.setItems(items);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }

    }

    @FXML
    private void handleServerButton(ActionEvent actionEvent) {

        try {

            // Open a new one
            FXMLLoader loader = new FXMLLoader(getClass().getResource("serverOptions.fxml"));
            AnchorPane pane = loader.load();

            Scene scene = new Scene(pane);
            Stage stage = new Stage();

            stage.setTitle("Server options menu");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @FXML
    private void handleSQLButton(ActionEvent actionEvent) {

        String sqlQuery = "";

        TextInputDialog dialogOne = new TextInputDialog("0");
        dialogOne.setTitle("SQL Query executor");
        dialogOne.setHeaderText("Provide SQL query which you would like to execute");
        dialogOne.setContentText("Query:");

        Optional<String> resultOne = dialogOne.showAndWait();

        if(resultOne.isPresent())
            sqlQuery = resultOne.get();

        // Make SQL connection
        Connection conn = JDBC_Helper.connect();
        Statement statement = JDBC_Helper.statement(conn);

        JDBC_Helper.executeQuery(statement, "USE tanks;");

        JDBC_Helper.executeQuery(statement, sqlQuery);

        JDBC_Helper.close(conn, statement);

    }

    @FXML
    private void handleExitButton(ActionEvent actionEvent) {

        Stage mainStage = (Stage) exitButton.getScene().getWindow();

        mainStage.close();
    }

}
