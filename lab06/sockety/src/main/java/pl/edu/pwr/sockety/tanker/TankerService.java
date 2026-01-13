package pl.edu.pwr.sockety.tanker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import static pl.edu.pwr.sockety.utils.GuiHelpers.showError;

public class TankerService {

    private Runnable onUpdateListener;
    private final int port;
    private final int capacity;
    private int currentVolume = 0;
    private final String officeIp;
    private final int officePort;
    private final ServerSocket serverSocket;
    private final String sewageIp;
    private final int sewagePort;

    public TankerService(int port, int capacity, int officePort, String officeIp, int sewagePort, String sewageIp) {
        this.port = port;
        this.capacity = capacity;
        this.officePort = officePort;
        this.officeIp = officeIp;
        this.sewagePort = sewagePort;
        this.sewageIp = sewageIp;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        register();
        startServer();
    }

    private void startServer() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Socket connection = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String request = in.readLine();

                    if (request != null && request.startsWith("j:")) {
                        String[] parts = request.split(":")[1].split(",");
                        int housePort = Integer.parseInt(parts[0]);
                        String houseIp = parts[1];

                        processJob(houseIp, housePort);
                    }
                    connection.close();
                } catch (Exception e) {
                    showError("Error receiving job");
                }
            }
        });
        thread.start();
    }

    private void processJob(String houseIp, int housePort) {
        try {
            notifyUpdate();
            Thread.sleep(2000);

            try (Socket houseSocket = new Socket(houseIp, housePort)) {
                PrintWriter out = new PrintWriter(houseSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(houseSocket.getInputStream()));

                int spaceLeft = capacity - currentVolume;
                out.println("gp:" + spaceLeft);

                String response = in.readLine();
                if (response != null) {
                    int pumped = Integer.parseInt(response);
                    this.currentVolume += pumped;
                    notifyUpdate();
                }
            }

            Thread.sleep(2000);
            setReady();
            notifyUpdate();

        } catch (Exception e) {
            showError("Error processing job");
            setReady();
            notifyUpdate();
        }
    }

    private void register() {
        sendToOffice("r");
    }

    private void setReady() {
        sendToOffice("sr");
    }

    private void sendToOffice(String prefix) {
        try (Socket socket = new Socket(officeIp, officePort)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String localIp = socket.getLocalAddress().getHostAddress();
            out.println(prefix + ":" + port + "," + localIp);

            in.readLine();
        } catch (IOException e) {
            showError("Could not contact office");
        }
    }

    public void setOnUpdateListener(Runnable listener) {
        this.onUpdateListener = listener;
    }

    private void notifyUpdate() {
        if (onUpdateListener != null) {
            onUpdateListener.run();
        }
    }

    public void emptyTank() {
        try (Socket socket = new Socket(this.sewageIp, this.sewagePort)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("spi:" + this.port + "," + currentVolume);

            String response = in.readLine();
            if (response != null && response.equals("1")) {
                this.currentVolume = 0;
                notifyUpdate();
            }
        } catch (IOException e) {
            showError("Could not connect to Sewage Plant at " + sewageIp + ":" + sewagePort);
        }
    }

    public int getCurrentVolume() { return currentVolume; }
    public int getCapacity() { return capacity; }
}