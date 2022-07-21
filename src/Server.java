import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.io.IOException;

public class Server {
    private ServerSocket soc_s;

    public Server(ServerSocket soc_s) {
        this.soc_s = soc_s;
    }

    public void start_server() {
        try {
            while (!this.soc_s.isClosed()) {
                Socket soc = soc_s.accept();
                System.out.println("A new client has connected.");
                ClientHandler client_handler = new ClientHandler(soc);

                Thread thread = new Thread(client_handler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeServerSocket() {
        try {
            if (this.soc_s != null) {
                soc_s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws SocketException {
        try {
            ServerSocket serversocket = new ServerSocket(8888);
            Server server = new Server(serversocket);
            server.start_server();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}