package sample;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;


public class ClientTCP_Listener {

    private ConnectionToServer server;
    private LinkedBlockingQueue<Object> messages;
    private Socket socket;

    public ClientTCP_Listener(String IPAddress, int port) throws IOException {

        socket = new Socket(IPAddress, port);
        messages = new LinkedBlockingQueue<Object>();
        server = new ConnectionToServer(socket);

        System.out.println("New Client created!");

        Thread messageHandling = new Thread(() -> {

            while(true) {

                System.out.println("In while..");

                try {

                    Object message = messages.take();

                    System.out.println("Klient: " + message.toString());

                    String[] messageSplit = message.toString().split(";");

                    switch(messageSplit[0]) {

                        case "math":

                            int add = Integer.parseInt(messageSplit[1]);
                            int minus = Integer.parseInt(messageSplit[2]);
                            int multiply = Integer.parseInt(messageSplit[3]);
                            double substract = Double.parseDouble(messageSplit[4]);

                            Platform.runLater(() -> {

                                try {

                                    // Open a new one
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("mathResult.fxml"));
                                    AnchorPane pane = loader.load();

                                    mathResultController mathResultController = loader.getController();

                                    mathResultController.populateLabels(add, minus, multiply, substract);

                                    Scene scene = new Scene(pane);
                                    Stage stage = new Stage();

                                    stage.setTitle("Server result");
                                    stage.setScene(scene);
                                    stage.show();

                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            });

                            break;

                        case "string":

                            String text = messageSplit[1];

                            Platform.runLater(() -> {

                                try {

                                    // Open a new one
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("stringResult.fxml"));
                                    AnchorPane pane = loader.load();

                                    stringResultController stringResultController = loader.getController();

                                    stringResultController.setReplyTextLabel(text);

                                    Scene scene = new Scene(pane);
                                    Stage stage = new Stage();

                                    stage.setTitle("Server result");
                                    stage.setScene(scene);
                                    stage.show();

                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            });
                            break;

                        default:
                            break;

                    }


                }

                catch(InterruptedException ignored){ }
            }
        });

        messageHandling.setDaemon(true);
        messageHandling.start();
    }

    private class ConnectionToServer {

        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;

        ConnectionToServer(Socket socket) throws IOException {


            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Thread read = new Thread(() -> {

                while(true){

                    try{

                        //System.out.println("ConnectionToServer reading..");
                        Object obj = in.readObject();
                        messages.put(obj);

                    }
                    catch(Exception ignored){ }
                }

            });

            read.setDaemon(true);
            read.start();
        }

        private void write(Object obj) {

            try{

                out.writeObject(obj);

            }

            catch(IOException e){ e.printStackTrace(); }
        }


    }

    public void send(Object obj) {
        server.write(obj);
    }
}
