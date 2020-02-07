package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ServerTCP_New {

    private ArrayList<ConnectionToClient> clientList;
    private LinkedBlockingQueue<Object> messages;
    private ServerSocket serverSocket;

    private ReentrantLock lock = new ReentrantLock();

    public ServerTCP_New(int port) throws IOException {

        clientList = new ArrayList<ConnectionToClient>();
        messages = new LinkedBlockingQueue<Object>();
        serverSocket = new ServerSocket(port);

        Thread accept = new Thread(() -> {

            while(true){

                try{

                    System.out.println("Server up & running");

                    Socket s = serverSocket.accept();
                    clientList.add(new ConnectionToClient(s));

                }

                catch(IOException e){ e.printStackTrace(); }
            }
        });

        accept.setDaemon(true);
        accept.start();

        Thread messageHandling = new Thread(() -> {

            while(true){

                try{

                    Object message = messages.take();

                    System.out.println("Serwer: " + message.toString());

                    String[] messageSplit = message.toString().split(";");

                    switch (messageSplit[0]) {

                        case "math":

                            int numOne = Integer.parseInt(messageSplit[1]);
                            int numTwo = Integer.parseInt(messageSplit[2]);

                            int add = numOne + numTwo;
                            int minus = numOne - numTwo;
                            int multiply = numOne * numTwo;
                            double substract;

                            if(numTwo != 0)
                                substract = numOne / numTwo;
                            else
                                substract = 0;

                            sendToAll("math;" + add + ";" + minus + ";" + multiply + ";" + substract);
                            break;

                        case "string":

                            String text;
                            String wordToReplace;
                            String word;

                            text = messageSplit[1];
                            wordToReplace = messageSplit[2];
                            word = messageSplit[3];

                            sendToAll("string;" + text.replace(wordToReplace, word));
                            break;

                        default:
                            break;

                    }

                }
                catch(InterruptedException e){ }
            }
        });

        messageHandling.setDaemon(true);
        messageHandling.start();
    }

    private class ConnectionToClient {

        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;
        int connectionID;

        ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            Thread read = new Thread(){
                public void run(){

                    while(true){

                        try{

                            Object obj = in.readObject();
                            messages.put(obj);

                        }
                        catch(Exception e){ e.printStackTrace(); }
                    }
                }
            };

            read.setDaemon(true); // terminate when main ends
            read.start();
        }

        public void write(Object obj) {
            try{
                out.writeObject(obj);
            }
            catch(IOException e){ e.printStackTrace(); }
        }

        public void assignConnectionID(int mID) {
            this.connectionID = mID;
        }
    }

    public void sendToOne(int index, Object message)throws IndexOutOfBoundsException {
        clientList.get(index).write(message);
    }

    public void sendToAll(Object message){

        for(ConnectionToClient client : clientList)
            client.write(message);

    }

}