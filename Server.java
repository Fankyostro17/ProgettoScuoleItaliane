import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.TreeMap;

public class Server extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private Socket clientSocket;
    private TreeMap<String, LinkedList<String>> archive;
    private LinkedList<String> titles;

    final static String PATH = "Anagrafe-delle-scuole-italiane.csv";

    public Server(Socket clientSocket) {
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
            out.println("Benvenuto nel Progetto Scuole Italiane!");
            out.println(codes());
            out.flush();
            while (true) {
                out.print("Insert Code -> ");
                String str = in.readLine();
                if (str.contains("")) {
                    String msgTmp = str.substring(0, 1);
                    for (int i = 1; i < str.length(); i++) {
                        msgTmp += str.substring(i, i+1);
                        if (!str.substring(i+1, i+2).equalsIgnoreCase("")) {
                            i++;
                        } else {
                            //msgTmp = msgTmp.substring(0, msgTmp.length()-1);
                        }
                    }
                    str = msgTmp;
                }
                if (str.equals("END") || str.equals("STOP")) {
                    break;
                }
                try {
                    if (str.substring(0, 10).equals("GET-COMUNE")) {
                        for (LinkedList<String> list : getArchive().values()) {
                            if (list.getFirst().contains(str.substring(13))) {
                                out.println(list.toString().replace(";", ", ")
                                .replace("[", "").replace("]", ""));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Server closing...");
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Accept failed");
            System.exit(1);
        }
    }

    private String codes(){
        return "Codici possibili:\nEND,\tSTOP,\tGET-COMUNE,\tGET-PROVINCIA,\tGET-REGIONE,\tGET-CODICE,\tGET-ISTITUTO,\tGET-TIPOLOGIA-ISTITUTO,\tGET-INDIRIZZO,\tGET-CODICE-POSTALE,\tGET-TELEFONO,\tGET-FAX,\tGET-EMAIL,\tGET-EMAIL-PEC,\tGET-DIREZIONE,\tGET-STATALE,\tGET-PARITARIA";
    }

    private void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String[] data = br.readLine().split(";");
            for (String titolo : data) {
                getTitles().add(titolo);
            }
            String line = br.readLine();
            while (!line.equalsIgnoreCase("")) {
                data = line.split(";");
                LinkedList<String> listaDati = new LinkedList<String>();
                for (String valore : data) {
                    listaDati.add(valore.replace("E'", "È")
                    .replace("O'", "Ò")
                    .replace("U'", "Ù")
                    .replace("A'", "À")
                    .replace("I'", "Ì"));
                }
                getArchive().put((String) listaDati.get(3), listaDati);
                line = br.readLine();
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
