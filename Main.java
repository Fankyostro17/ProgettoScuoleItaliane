
import java.io.IOException;
import java.net.Socket;

public class Main {

    final static int PORT = 1717;
    final static String serverAddress = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        try (Socket clientSocket = new Socket(serverAddress, PORT)) {
            System.out.println("Connessione al server: " + serverAddress + ":" + PORT);

            Client client = new Client(clientSocket);
            client.start();

            client.join();
        } catch (Exception e) {
            System.out.println("Errore del Client.");
        }
    }
}
