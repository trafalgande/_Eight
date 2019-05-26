package lmao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Socket clientSocket = null;
    private final ObjectInputStream in = null;
    private final PrintWriter out = null;

    public Server(final int port) throws IOException {
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            System.err.println("Server is running.");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    serverSocket.close();
                } catch (final IOException | NullPointerException e) {
                }
            }));
            while (true) {
                clientSocket = serverSocket.accept();
                System.err.println("Client was accepted.");
                final ServerThread serverThread = new ServerThread(clientSocket);
                serverThread.start();
            }
        }
    }

    public static void main(final String[] args) {
        try {
            new Server(8200);
        } catch (final Exception mex) {
            System.out.println(mex);
        }
    }
}
