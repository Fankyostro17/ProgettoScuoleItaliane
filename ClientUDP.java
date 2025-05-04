
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {

    public static final int PORT_UDP = 1771;
    public static final String SERVER_ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        Scanner kbd = new Scanner(System.in);
        DatagramSocket clientSocket = null;
        byte[] datiInviati = new byte[1024];
        byte[] datiRicevuti = new byte[1024];

        try {
            InetAddress IPAddress = InetAddress.getByName(SERVER_ADDRESS);
            clientSocket = new DatagramSocket();

            datiInviati = "START".getBytes();

            DatagramPacket pacchettoInviato = new DatagramPacket(datiInviati, datiInviati.length, IPAddress, PORT_UDP);
            clientSocket.send(pacchettoInviato);

            DatagramPacket pacchettoRicevuto = new DatagramPacket(datiRicevuti, datiRicevuti.length);
            clientSocket.receive(pacchettoRicevuto);

            String str = new String(pacchettoRicevuto.getData(), 0, pacchettoRicevuto.getLength()).trim();
            System.out.println(str);
            while (true) {
                String comando = kbd.nextLine();
                datiInviati = comando.getBytes();

                pacchettoInviato = new DatagramPacket(datiInviati, datiInviati.length, IPAddress, PORT_UDP);
                clientSocket.send(pacchettoInviato);

                if (comando.equalsIgnoreCase("END")) {
                    System.out.println("Chiusura del client.");
                    break;
                } else if (comando.equalsIgnoreCase("STOP")) {
                    System.out.println("Chiusura del client e del server.");
                    break;
                }

                try {
                    do {
                        pacchettoRicevuto = new DatagramPacket(datiRicevuti, datiRicevuti.length);
                        clientSocket.receive(pacchettoRicevuto);

                        str = new String(pacchettoRicevuto.getData(), 0, pacchettoRicevuto.getLength()).trim();
                        if (!str.equalsIgnoreCase("F")) {
                            System.out.println(str);
                        }
                    } while (!str.equalsIgnoreCase("F"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}
