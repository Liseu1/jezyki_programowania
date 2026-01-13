package pl.edu.pwr.sockety.office;

import pl.edu.pwr.sockety.office.model.HouseRequest;
import pl.edu.pwr.sockety.office.model.Tanker;
import pl.edu.pwr.sockety.utils.IpValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.pwr.sockety.utils.GuiHelpers.showError;

public class OfficeService {

    private ArrayList<Tanker> tankers = new ArrayList<>();
    private ArrayList<HouseRequest> requests = new ArrayList<>();

    private final ServerSocket serverSocket;

    private final String sewageIp;
    private final int sewagePort;

    private Runnable onUpdateListener;

    public OfficeService(int port, String sewageIp, int sewagePort) {
        this.sewageIp = sewageIp;
        this.sewagePort = sewagePort;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public void startServer() {
        Thread connectionHandler = new Thread(() -> {
            while(true) {
                try {
                    Socket connection = serverSocket.accept();
                    PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String request = in.readLine();

                    if(request != null) {
                        int clientPort;
                        String clientIp = "";
                        try {
                            String[] parts = request.split(":")[1].split(",");
                            clientPort = Integer.parseInt(parts[0]);
                            if(IpValidator.validate(parts[1])) {
                                clientIp = parts[1];
                            }
                        } catch (Exception e) {
                            showError("Invalid port/ip inside request");
                            continue;
                        }

                        boolean success = false;
                        switch (request.charAt(0)) {
                            case 'o':
                                requests.add(new HouseRequest(clientIp, clientPort));
                                notifyUpdate();
                                success = true;
                                break;
                            case 'r':
                                success = register(clientIp, clientPort);
                                notifyUpdate();
                                break;
                            case 's':
                                if(request.charAt(1) == 'r') {
                                    success = setReady(clientIp, clientPort);
                                    notifyUpdate();
                                }
                                break;
                            default:
                                showError("Bad request");
                        }

                        if(success) {
                            out.println("1");
                        } else {
                            out.println("0");
                        }
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error processing request");
                }
            }
        });
        connectionHandler.start();
    }

    public boolean sendTanker(Tanker tanker, HouseRequest request) {
        if (!tanker.isReady) {
            return false;
        }

        tanker.isReady = false;

        requests.remove(request);
        notifyUpdate();

        try {
            Socket socket = new Socket(tanker.getIp(), tanker.getPort());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("j:" + request.port() + "," + request.ip());
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            tanker.isReady = true;
            requests.add(request);
            notifyUpdate();
            return false;
        }
    }

    private boolean register(String ip, int port) {
        for(Tanker t : tankers) {
            if(t.getIp().equals(ip) && t.getPort() == port) return true;
        }
        this.tankers.add(new Tanker(ip, port));
        return true;
    }

    private boolean setReady(String ip, int port) {
        for (Tanker t : tankers) {
            if (t.getIp().equals(ip) && t.getPort() == port) {
                t.isReady = true;
                return true;
            }
        }
        return false;
    }

    public String getTankerStatus(int tankerId) {
        try (Socket socket = new Socket(sewageIp, sewagePort)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("gs:" + tankerId);
            return in.readLine();
        } catch (IOException e) {
            return "Błąd połączenia";
        }
    }

    public boolean payOffTanker(int tankerId) {
        try (Socket socket = new Socket(sewageIp, sewagePort)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("spo:" + tankerId);
            String response = in.readLine();
            return "1".equals(response);
        } catch (IOException e) {
            return false;
        }
    }

    public List<Tanker> getTankers() { return tankers; }
    public List<HouseRequest> getRequests() { return requests; }
}