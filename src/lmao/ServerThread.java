package lmao;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ServerThread extends Thread {
    Socket s = null;
    ObjectInputStream in = null;
    PrintWriter out = null;
    SocketDto msg;
    DataBaseManager income = new DataBaseManager();
    CommandHandler start = new CommandHandler(income);
    public ServerThread (Socket s) {
        this.s = s;
    }

    public void run() {

        try {
            in = new ObjectInputStream(s.getInputStream());
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
        } catch (IOException mex) {
            System.out.println(mex);
        }
        try{
            while ((msg = (SocketDto) in.readObject()) != null) {
                out.println(start.init(msg));
                if (msg.equals("exit")) {
                    out.println(start.init(msg));
                    s.close();
                }
            }
        }   catch (IOException | ClassNotFoundException | SQLException mex) {
            System.out.println(mex);
        }


    }
}

