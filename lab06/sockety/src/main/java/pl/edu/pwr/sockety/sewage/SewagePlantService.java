package pl.edu.pwr.sockety.sewage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import pl.edu.pwr.sockety.sewage.model.Payment;
import pl.edu.pwr.sockety.utils.GuiHelpers;

public class SewagePlantService {

    private final ServerSocket serverSocket;
    private final ConcurrentHashMap<Integer, Integer> tankerStorage = new ConcurrentHashMap<>();
    private final List<Payment> paymentHistory = Collections.synchronizedList(new ArrayList<>());
    private Runnable onUpdateListener;

    public SewagePlantService(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startServer();
    }

    public void setOnUpdateListener(Runnable listener) {
        this.onUpdateListener = listener;
    }

    private void notifyUpdate() {
        if (onUpdateListener != null) onUpdateListener.run();
    }

    private void startServer() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Socket connection = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    PrintWriter out = new PrintWriter(connection.getOutputStream(), true);

                    String request = in.readLine();
                    if (request != null) {
                        handleRequest(request, out);
                    }
                    connection.close();
                } catch (Exception e) {
                    GuiHelpers.showError("Error receiving request in Sewage Plant");
                }
            }
        });
        thread.start();
    }

    private void handleRequest(String request, PrintWriter out) {
        String[] parts = request.split(":");
        String method = parts[0];
        String[] args = parts.length > 1 ? parts[1].split(",") : new String[0];

        switch (method) {
            case "spi":
                int tankerNumber = Integer.parseInt(args[0]);
                int volume = Integer.parseInt(args[1]);
                tankerStorage.merge(tankerNumber, volume, Integer::sum);
                out.println("1");
                notifyUpdate();
                break;

            case "gs":
                int targetTanker = Integer.parseInt(args[0]);
                int currentVolume = tankerStorage.getOrDefault(targetTanker, 0);
                out.println(currentVolume);
                break;

            case "spo":
                int payOffTanker = Integer.parseInt(args[0]);
                int debt = tankerStorage.getOrDefault(payOffTanker, 0);

                if (debt > 0) {
                    paymentHistory.add(new Payment(payOffTanker, debt));
                    tankerStorage.put(payOffTanker, 0);
                }
                out.println("1");
                notifyUpdate();
                break;

            default:
                out.println("0");
                break;
        }
    }

    public String getStorageSummary() {
        if (tankerStorage.isEmpty()) return "Brak nieopłaconych ścieków.";
        return tankerStorage.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(e -> "Cysterna #" + e.getKey() + ": " + e.getValue() + " L")
                .collect(Collectors.joining("\n"));
    }

    public String getHistorySummary() {
        if (paymentHistory.isEmpty()) return "Brak historii płatności.";
        List<Payment> reversed = new ArrayList<>(paymentHistory);
        Collections.reverse(reversed);
        return reversed.stream().map(Payment::toString).collect(Collectors.joining("\n"));
    }
}