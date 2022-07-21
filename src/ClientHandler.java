import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> client_handlers = new ArrayList<>();
    private Socket soc;
    private BufferedReader buff_reader;
    private BufferedWriter buff_writer;
    private String cli_username;

    public ClientHandler(Socket soc) {
        try {
            this.soc = soc;
            this.buff_writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
            this.buff_reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            this.cli_username = buff_reader.readLine();
            client_handlers.add(this);
            broadcastMessage("Server : " + cli_username + " has entered the chat !");

        } catch (IOException e) {
            closeEverything(soc, buff_reader, buff_writer);
        }

    }

    @Override
    public void run() {

        String msg;

        while (soc.isConnected()) {
            try {
                msg = buff_reader.readLine();
                broadcastMessage(msg);
            } catch (IOException e) {
                closeEverything(soc, buff_reader, buff_writer);
                break;
            }
        }
    }

    public void broadcastMessage(String msg) {
        for (ClientHandler client_handler : client_handlers) {
            try {
                if (!client_handler.cli_username.equals(cli_username)) {
                    client_handler.buff_writer.write(msg);
                    client_handler.buff_writer.newLine();
                    client_handler.buff_writer.flush();
                }
            } catch (IOException e) {
                closeEverything(soc, buff_reader, buff_writer);
            }
        }
    }

    public void removeClientHandler() {
        client_handlers.remove(this);
        broadcastMessage("Server : " + cli_username + " has left.");
    }

    public void closeEverything(Socket soc,  BufferedReader buff_reader,
                                BufferedWriter buff_writer) {
        removeClientHandler();
        try {
            if (buff_reader != null) {
                buff_reader.close();
            }
            if (buff_writer != null) {
                buff_writer.close();
            }
            if (soc != null) {
                soc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}