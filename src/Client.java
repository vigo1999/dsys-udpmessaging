import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.lang.InterruptedException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private DatagramSocket dsoc;
    private InetAddress inet;
    private byte[] buffer;

    public Client(DatagramSocket dsoc, InetAddress inet) {
        this.dsoc = dsoc;
        this.inet = inet;

    }

    public void sendThenReceive() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String messageToSend = scanner.nextLine();
                buffer = messageToSend.getBytes();
                DatagramPacket dpac = new DatagramPacket(buffer, buffer.length, inet, 8888);
                dsoc.send(dpac);
                dsoc.receive(dpac);
                String msgFromServer = new String(dpac.getData(), 0, dpac.getLength());
                System.out.println("Server Said you said : " + msgFromServer);
            } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
        }
    }

    public static void main(String[] args) throws SocketException {
        try {
            DatagramSocket dsoc = new DatagramSocket();
            InetAddress inet = InetAddress.getLocalHost();
            Client client = new Client(dsoc, inet);
            System.out.println("Send datagram packet to a server.");
            client.sendThenReceive();
        } catch (UnknownHostException e) {
            e.printStackTrace();

        }

    }
}