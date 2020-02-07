package sample;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.sql.*;

public class JDBC_Helper {

    public static Connection connect() {
        Connection connection = null;
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306";
            String username = "connector";
            String password = "admin";
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("Błąd połączenia z MySQL! " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
        System.out.println("Połączono z serwerem MySQL");

        return connection;
    }

    public static Statement statement(Connection connect) {
        Statement statement = null;
        try {
            statement = connect.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    public static Connection close(Connection connect, Statement statement) {
        try {
            statement.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int executeUpdate(Statement statement, String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static ResultSet executeQuery(Statement statement, String sql) {
        try {

            return statement.executeQuery(sql);

        }catch (SQLException e){

            Platform.runLater(() -> {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL Error");
                alert.setHeaderText("SQL Service replied with an error:");
                alert.setContentText(e.getMessage());

                alert.showAndWait();

            });
            //e.printStackTrace();

        }
        return null;
    }

}
