import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.LinkedList;
import java.util.TreeMap;

public class Server extends Thread{
    private BufferedReader in;
    private PrintWriter out;
    private Socket clientSocket;
    private TreeMap<String, LinkedList<String>> archive;
    private LinkedList<String> titles;

    final static String PATH = "Anagrafe-delle-scuole-italiane.csv";

    public Server(Socket clientSocket){
        setIn(null);
        setOut(null);
        setClientSocket(clientSocket);
        setArchive(new TreeMap<String, LinkedList<String>>());
        setTitles(new LinkedList<String>());
    }

    @Override
    public void run() {
        try {
            System.out.println("Connection accepted: " + clientSocket);
            readFile();
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            in = new BufferedReader(isr);
            OutputStreamWriter osw = new OutputStreamWriter(clientSocket.getOutputStream());
            BufferedWriter bw = new BufferedWriter(osw);
            out = new PrintWriter(bw, true);
            out.print("Hello (END or STOP to close connection), calcolo fattoriale inserisci numero: ");
            out.flush();
            while (true) {
                String str = in.readLine();
                BigInteger fattoriale = BigInteger.ONE;
                if (str.equals("END") || str.equals("STOP"))
                    break;
                try {
                    int number = Integer.parseInt(str);
                    for (int i = 2; i <= number; i++) {
                        fattoriale = fattoriale.multiply(BigInteger.valueOf(i));
                    }
                } catch (Exception e) {
                    fattoriale = BigInteger.ZERO;
                    System.out.println("Non e' un numero");
                    out.println("Non e' un numero");
                }
                System.out.println("Fattoriale calcolato: " + fattoriale);
                out.println("Fattoriale calcolato: " + fattoriale);
            }
            System.out.println("EchoServer: closing...");
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Accept failed");
            System.exit(1);
        }
    }

    public void readFile(){
        try (BufferedReader br = new BufferedReader(new FileReader(PATH))){
            String[] data = br.readLine().split(";");
            for (String titolo : data) {
                getTitles().add(titolo);
            }
            String line = br.readLine();
            while (!line.equalsIgnoreCase("")) {
                data = line.split(";");
                LinkedList listaDati = new LinkedList<String>();
                for (String valore : data) {
                    listaDati.add(valore);
                }
                getArchive().put((String) listaDati.get(1), listaDati);
            }
        } catch (Exception e) {
        }
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public TreeMap<String, LinkedList<String>> getArchive() {
        return archive;
    }

    public void setArchive(TreeMap<String, LinkedList<String>> archive) {
        this.archive = archive;
    }

    public LinkedList<String> getTitles() {
        return titles;
    }

    public void setTitles(LinkedList<String> titles) {
        this.titles = titles;
    }
}
