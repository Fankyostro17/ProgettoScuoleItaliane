
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner consoleScanner;

    public static final int PORT = 1717;

    public Client(Socket socket) {
        try {
            setSocket(socket);
        } catch (Exception e) {
            System.out.println("Errore nella conessione al server: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Connection accepted: " + getSocket());

            setOut(new PrintWriter(new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream())), true));
            setIn(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            setConsoleScanner(new Scanner(System.in));

            String line;
            while (!(line = getIn().readLine()).startsWith("Insert Code ->")) {
                System.out.println(line);
            }

            System.out.print(line);

            while (true) {
                String str = getConsoleScanner().nextLine();
                if (str.equals("END")) {
                    break;
                } else if (str.equals("STOP")) {
                    getOut().println(str);
                    getOut().flush();
                    break;
                }
                getOut().println(str);
                getOut().flush();

                while (!(line = getIn().readLine()).startsWith("Insert Code ->")) {
                    System.out.println(line);
                }

                System.out.print(line);
            }

            getSocket().close();
            getIn().close();
            getOut().close();
        } catch (Exception e) {
            System.err.println("Errore durante la comunicazione con il server: " + e.getMessage());
        }
    }

    public static int getPort() {
        return PORT;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public Scanner getConsoleScanner() {
        return consoleScanner;
    }

    public void setConsoleScanner(Scanner consoleScanner) {
        this.consoleScanner = consoleScanner;
    }

    public static void main(String[] args) throws IOException {
        int PORT = 1717;
        String serverAddress = "127.0.0.1";

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
