package sample;

import java.io.*;
import java.util.ArrayList;

public class multiThread implements Runnable {

    private Thread thread;

    private File file;

    private ArrayList<String> strArray = new ArrayList<>();

    public ArrayList<String> getArray() {

        return strArray;
    }

    public multiThread(int numOfThreads, File mFile) {

        this.file = mFile;

        for(int i = 0; i < numOfThreads; i++) {
            thread = new Thread();
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    public void run() {

        readFile(this.file);

    }

    private synchronized void readFile(File inFile) {

        try {

            String line;
            BufferedReader buf = new BufferedReader(new FileReader(inFile.getAbsolutePath()));
            while ((line = buf.readLine()) != null) {
                strArray.add(line);
                Thread.yield();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}