import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.util.Scanner;
public class Client {

    private Socket soc;
    private BufferedReader buff_reader;
    private BufferedWriter  buff_writer;
    private String username;

    public Client(Socket soc, String username) {
        try {
            this.soc = soc;
            this.buff_writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
            this.buff_reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(soc, buff_reader, buff_writer);
        }
    }

    public void sendMessage() {
        try {
            buff_writer.write(username);
            buff_writer.newLine();
            buff_writer.flush();

            Scanner sc = new Scanner(System.in);

            while (soc.isConnected()) {
                String msg = sc.nextLine();
                buff_writer.write(username + ": " + msg);
                buff_writer.newLine();
                buff_writer.flush();
            }
        } catch (IOException e) {
            closeEverything(soc, buff_reader, buff_writer);
        }
    }

    public void listenForMessage() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // message from group chat
                String msg;
                // wait for a new message
                while (soc.isConnected()) {
                    try {
                        msg = buff_reader.readLine();
                        System.out.println(msg);
                    } catch (IOException e) {
                        closeEverything(soc, buff_reader, buff_writer);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket soc,  BufferedReader buff_reader, BufferedWriter buff_writer) {
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
     public static void main(String[] args) throws SocketException {

       try {
           Scanner sc = new Scanner(System.in);
           System.out.println("Enter your username :");
           String username = sc.nextLine();
           Socket soc = new Socket("localhost",8888);
           Client client = new Client(soc, username);
           client.listenForMessage();
           client.sendMessage();
       } catch (UnknownHostException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }

     }
}