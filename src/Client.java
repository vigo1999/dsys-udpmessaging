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

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter  bufferedWriter;
    private String username;

    public Client(Socket socket,String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = sc.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupchat;

                while (socket.isConnected()) {
                    try {
                        messageFromGroupchat = bufferedReader.readLine();
                        System.out.println(messageFromGroupchat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket,  BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
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
           Socket socket = new Socket("localhost",8888);
           Client client = new Client(socket, username);
           client.listenForMessage();
           client.sendMessage();
       } catch (UnknownHostException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }

     }
}