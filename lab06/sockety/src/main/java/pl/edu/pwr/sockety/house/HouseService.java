package pl.edu.pwr.sockety.house;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import static pl.edu.pwr.sockety.utils.GuiHelpers.showError;

public class HouseService {
    private final int port;
    public final int capacity;
    private final ServerSocket serverSocket;
    private int currentVolume = 0;
    private final String officeIp;
    private final int officePort;
    private volatile boolean ordered = false;
    private Runnable onUpdateListener;

    public HouseService(int port, int capacity, int officePort, String officeIp) {
        this.port = port;
        this.capacity = capacity;
        this.officePort = officePort;
        this.officeIp = officeIp;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        simulateWaste();
        startServer();
    }

    public void setOnUpdateListener(Runnable listener) {
        this.onUpdateListener = listener;
    }

    private void notifyUpdate() {
        if (onUpdateListener != null) {
            onUpdateListener.run();
        }
    }

    public void startServer(){
        Thread connectionHandler = new Thread(() -> {
            while(true) {
                try {
                    Socket connection = serverSocket.accept();
                    PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String request = in.readLine();
                    if (request != null && request.startsWith("gp")) {
                        int max = Integer.parseInt(request.split(":")[1]);
                        int actualPumped = Math.min(max, currentVolume);
                        this.currentVolume -= actualPumped;
                        out.println(actualPumped);
                        connection.close();
                        ordered = false;
                        notifyUpdate();
                    }
                } catch (Exception e) {
                    showError("Error processing request");
                }
            }
        });
        connectionHandler.start();
    }

    private void simulateWaste() {
        Thread wasteSimulation = new Thread(() -> {
            while(true) {
                if(this.currentVolume >= this.capacity * 0.9 && !ordered) {
                    ordered = true;
                    notifyUpdate();
                    order();
                }
                if(this.currentVolume + 1 >= this.capacity){
                    this.currentVolume = this.capacity;
                } else {
                    this.currentVolume += 1;
                }
                notifyUpdate();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    showError("Error simulating waste");
                }
            }
        });
        wasteSimulation.start();
    }

    private void order() {
        try (Socket clientSocket = new Socket(officeIp, officePort)){
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String output = "o:%d,%s";
            String result = String.format(output, this.port, clientSocket.getLocalAddress().getHostAddress());
            out.println(result);
            String response = in.readLine();
            if (response == null || !response.equals("1")) {
                ordered = false;
            }
        } catch (IOException e) {
            ordered = false;
            showError("Error sending request");
        }
        notifyUpdate();
    }

    public int getCurrentVolume() {
        return this.currentVolume;
    }

    public boolean isOrdered() {
        return ordered;
    }
}