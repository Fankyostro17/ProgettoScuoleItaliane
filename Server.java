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
            System.out.println("Connection accepted: " + getClientSocket());
            readFile();
            setIn(new BufferedReader(new InputStreamReader(getClientSocket().getInputStream())));
            setOut(new PrintWriter(new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream())), true));
            getOut().println("Benvenuto nel Progetto Scuole Italiane!");
            getOut().println(codes());
            getOut().flush();
            while (true) {
                getOut().print("Insert Code -> ");
                String str = in.readLine();
                if (str.contains("")) {
                    str = changeCode(str);
                }
                if (str.equals("END") || str.equals("STOP")) {
                    break;
                }
                try {
                    if (str.substring(0, 10).equals("GET-COMUNE")) {
                        comand("GET-COMUNE", str, 0);
                    }
                    if (str.substring(0, 13).equals("GET-PROVINCIA")) {
                        comand("GET-PROVINCIA", str, 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Server closing...");
            getOut().close();
            getIn().close();
            getClientSocket().close();
        } catch (IOException e) {
            System.err.println("Accept failed");
            System.exit(1);
        }
    }

    private void comand(String comand, String str, int index){
        for (LinkedList<String> list : getArchive().values()) {
            if (str.substring(comand.length()+2).contains(list.get(index))) {
                getOut().println(list.toString().replace(";", ", ")
                .replace("[", "").replace("]", "") + "\n");
            }
        }
    }

    private String changeCode(String str){
        String msgTmp = str.substring(0, 1);
        for (int i = 1; i < str.length(); i++) {
            String charTmp = str.substring(i, i+1);
            if (charTmp.equalsIgnoreCase("")) {
                msgTmp = msgTmp.substring(0, msgTmp.length()-1);
            } else {
                msgTmp += charTmp;
            }
        }
        return msgTmp;
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
