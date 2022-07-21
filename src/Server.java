import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.io.IOException;

public class Server {
    private ServerSocket serversoc;

    public Server(ServerSocket serversoc) {
        this.serversoc = serversoc;
    }

    public void start_server() {
        try {
            while (!serversoc.isClosed()) {
                Socket socket = serversoc.accept();
                System.out.println("A new client has connected.");
                ClientHandler clienthandler = new ClientHandler(socket);

                Thread thread = new Thread(clienthandler);
                thread.start();
            }
        } catch (IOException e) {

        }
    }

    public void closeServerSocket() {
        try {
            if (serversoc != null) {
                serversoc.close();
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