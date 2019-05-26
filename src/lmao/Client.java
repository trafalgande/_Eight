package lmao;

import lombok.NonNull;
import sample.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private int port;
    private Socket clientSocket = null;
    public static BufferedReader in = null;
    public static ObjectOutputStream out = null;

    public Client(String address, int port) {
        SocketDto socketDto = new SocketDto();
        try {
            clientSocket = new Socket(address, port);
            System.err.println("You have been connected to server.");
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            //stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException mex) {
            System.out.println(mex);
        }
    }
}

