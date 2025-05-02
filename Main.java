import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static final int PORT = 1717;

    public static void main(String[] args) throws IOException {
        do {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("EchoServer: started ");
                System.out.println("Server Socket: " + serverSocket);
                Socket clientSocket = serverSocket.accept();
                Server server = new Server(clientSocket);
                server.start();
            } catch (Exception ex) {
                System.out.println("error");
            }
        } while (true);
    }
}
