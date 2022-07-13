import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.SocketException;
public class Server {

    private DatagramSocket dsoc;
    private byte[] buffer = new byte[256];

    public Server(DatagramSocket dsoc) {
        this.dsoc = dsoc;
    }

    public void receiveThenSend() {

        while (true) {
            try {
                DatagramPacket dpac = new DatagramPacket(buffer, buffer.length);
                dsoc.receive(dpac);
                InetAddress inet = dpac.getAddress();
                int port = dpac.getPort();
                String messageReceived = new String(dpac.getData(), 0, dpac.getLength());
                System.out.println("Message from client : " + messageReceived);
                dpac = new DatagramPacket(buffer, buffer.length, inet, port);
                dsoc.send(dpac);

            } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
        }
    }

    public static void main(String[] args) throws SocketException {
        DatagramSocket dsoc = new DatagramSocket(8888);
        Server server = new Server(dsoc);
        server.receiveThenSend();
    }
}
