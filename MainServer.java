
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.TreeMap;

public class MainServer {

    public static void main(String[] args) throws IOException {
        UDP_Server udp_server = new UDP_Server();
        TCP_Server tcp_server = new TCP_Server();

        Thread udp_server_thread = new Thread(udp_server);
        Thread tcp_server_thread = new Thread(tcp_server);

        udp_server_thread.start();
        tcp_server_thread.start();
    }
}

class UDP_Server implements Runnable {

    private TreeMap<String, LinkedList<String>> archive;
    private LinkedList<String> titles;

    final static int PORT_UDP = 1771;
    final static String PATH = "Anagrafe-delle-scuole-italiane.csv";

    public UDP_Server() {
        setArchive(new TreeMap<String, LinkedList<String>>());
        setTitles(new LinkedList<String>());
        readFile();
    }

    @Override
    public void run() {
        try {
            System.out.println("Connessione server UDP nella porta " + PORT_UDP);
            Server server = new Server(getArchive(), getTitles());
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    listaDati.add(valore/*.replace("E'", "È")
                            .replace("O'", "Ò")
                            .replace("U'", "Ù")
                            .replace("A'", "À")
                            .replace("I'", "Ì")*/);
                }
                getArchive().put((String) listaDati.get(3), listaDati);
                line = br.readLine();
            }
        } catch (Exception e) {

        }
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

class TCP_Server implements Runnable {

    private TreeMap<String, LinkedList<String>> archive;
    private LinkedList<String> titles;

    final static int PORT_TCP = 1717;
    final static String PATH = "Anagrafe-delle-scuole-italiane.csv";

    public TCP_Server() {
        setArchive(new TreeMap<String, LinkedList<String>>());
        setTitles(new LinkedList<String>());
        readFile();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT_TCP)) {
            System.out.println("Server started ");
            System.out.println("Server Socket: " + serverSocket);
            do {
                Socket clientSocket = serverSocket.accept();
                Server server = new Server(clientSocket, getArchive(), getTitles());
                server.start();
                /*Thread serverThread = new Thread(server);
                serverThread.start();*/
            } while (true);
        } catch (Exception ex) {
            System.out.println("error");
        }
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
                    listaDati.add(valore/*.replace("E'", "È")
                            .replace("O'", "Ò")
                            .replace("U'", "Ù")
                            .replace("A'", "À")
                            .replace("I'", "Ì")*/);
                }
                getArchive().put((String) listaDati.get(3), listaDati);
                line = br.readLine();
            }
        } catch (Exception e) {

        }
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

class Server extends Thread {

    private BufferedReader in;
    private PrintWriter out;
    private Socket clientSocket;
    private TreeMap<String, LinkedList<String>> archive;
    private LinkedList<String> titles;

    public Server(Socket clientSocket, TreeMap<String, LinkedList<String>> archive, LinkedList<String> titles) {
        setIn(null);
        setOut(null);
        setClientSocket(clientSocket);
        setArchive(archive);
        setTitles(titles);
    }

    public Server(TreeMap<String, LinkedList<String>> archive, LinkedList<String> titles) {
        setIn(null);
        setOut(null);
        setArchive(archive);
        setTitles(titles);
    }

    @Override
    public void run() {
        if (getClientSocket() != null) {
            TCP_Connection();
        } else {
            UDP_Connection();
        }
    }

    private void UDP_Connection() {
        try {
            DatagramSocket clientUDP_Socket = new DatagramSocket(Integer.parseInt("1771"));
            byte[] datiRicevuti = new byte[1024];
            byte[] datiInviati;

            DatagramPacket pacchettoRicevuto = new DatagramPacket(datiRicevuti, datiRicevuti.length);
            clientUDP_Socket.receive(pacchettoRicevuto);
            System.out.println("Connection UDP accepted");

            InetAddress IP_Address = pacchettoRicevuto.getAddress();
            int clientPort = pacchettoRicevuto.getPort();
            String msg = "Benvenuto nel Progetto Scuole Italiane!\n" + codes() + "\nInsert Code -> ";

            DatagramPacket pacchettoInviato = new DatagramPacket(msg.getBytes(), msg.getBytes().length, IP_Address, clientPort);
            clientUDP_Socket.send(pacchettoInviato);

            while (true) {
                datiRicevuti = new byte[1024];

                pacchettoRicevuto = new DatagramPacket(datiRicevuti, datiRicevuti.length);
                clientUDP_Socket.receive(pacchettoRicevuto);

                IP_Address = pacchettoRicevuto.getAddress();
                clientPort = pacchettoRicevuto.getPort();
                String str = new String(pacchettoRicevuto.getData(), 0, pacchettoRicevuto.getLength()).trim();

                if (str.equals("END") || str.equals("STOP")) {
                    break;
                }

                String msgOut = elencoComandi(str) + "\n" + codes() + "\nInsert Code -> ";

                datiInviati = msgOut.getBytes();

                int count = (datiInviati.length / 1024) + 1;
                int index = 1024;
                int arrayCount = 0;
                for (int i = 0; i < count; i++) {
                    byte[] tmp = new byte[index];
                    for (int j = 0; j < 1024; j++) {
                        tmp[j] = datiInviati[arrayCount];
                        arrayCount++;
                        if (arrayCount == datiInviati.length) {
                            break;
                        }
                    }
                    pacchettoInviato = new DatagramPacket(tmp, tmp.length, IP_Address, clientPort);
                    clientUDP_Socket.send(pacchettoInviato);
                }

                pacchettoInviato = new DatagramPacket("F".getBytes(), "F".getBytes().length, IP_Address, clientPort);
                clientUDP_Socket.send(pacchettoInviato);
            }

            clientUDP_Socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void TCP_Connection() {
        try {
            System.out.println("Connection accepted: " + getClientSocket());
            setIn(new BufferedReader(new InputStreamReader(getClientSocket().getInputStream())));
            setOut(new PrintWriter(new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream())), true));

            StringBuilder msg = new StringBuilder();
            msg.append("Benvenuto nel Progetto Scuole Italiane!\n");
            msg.append(codes());
            msg.append("\nInsert Code -> ");
            getOut().println(msg.toString());
            getOut().flush();

            while (true) {
                String str = in.readLine();
                if (str.contains("")) {
                    str = changeCode(str);
                }
                if (str.equals("END") || str.equals("STOP")) {
                    break;
                }
                try {
                    StringBuilder msgOut = new StringBuilder();
                    msgOut.append(elencoComandi(str));
                    msgOut.append(codes());
                    msgOut.append("\nInsert Code -> ");
                    getOut().println(msgOut.toString());
                    getOut().flush();
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

    private String elencoComandi(String str) {
        String msg = "";
        if (str.startsWith("GET-COMUNE")) {
            msg += comand("GET-COMUNE", str, 0);
        } else if (str.startsWith("GET-PROVINCIA")) {
            msg += comand("GET-PROVINCIA", str, 1);
        } else if (str.startsWith("GET-REGIONE")) {
            msg += comand("GET-REGIONE", str, 2);
        } else if (str.startsWith("GET-CODICE")) {
            msg += comand("GET-CODICE", str, 3);
        } else if (str.startsWith("GET-ISTITUTO")) {
            msg += comand("GET-ISTITUTO", str, 4);
        } else if (str.startsWith("GET-TIPOLOGIA-ISTITUTO")) {
            msg += comand("GET-TIPOLOGIA-ISTITUTO", str, 5);
        } else if (str.startsWith("GET-INDIRIZZO")) {
            msg += comand("GET-INDIRIZZO", str, 6);
        } else if (str.startsWith("GET-CODICE-POSTALE")) {
            msg += comand("GET-CODICE-POSTALE", str, 7);
        } else if (str.startsWith("GET-TELEFONO")) {
            msg += comand("GET-TELEFONO", str, 8);
        } else if (str.startsWith("GET-FAX")) {
            msg += comand("GET-FAX", str, 9);
        } else if (str.startsWith("GET-EMAIL")) {
            msg += comand("GET-EMAIL", str, 10);
        } else if (str.startsWith("GET-EMAIL-PEC")) {
            msg += comand("GET-EMAIL-PEC", str, 11);
        } else if (str.startsWith("GET-SITE")) {
            msg += comand("GET-SITE", str, 12);
        } else if (str.startsWith("GET-DIREZIONE")) {
            msg += comand("GET-DIREZIONE", str, 13);
        } else if (str.startsWith("GET-STATALE")) {
            msg += comand("GET-STATALE", str, 14);
        } else if (str.startsWith("GET-PARITARIA")) {
            msg += comand("GET-PARITARIA", str, 14);
        } else {
            msg += "Comando non valido.";
        }
        return msg;
    }

    private String comand(String comand, String str, int index) {
        String msg = "";
        str = str.toUpperCase();
        if (!comand.equalsIgnoreCase("GET-STATALE") && !comand.equalsIgnoreCase("GET-PARITARIA")) {
            if (!(str.charAt(comand.length() + 1) == ' ')) {
                str = str.substring(0, comand.length() + 1) + " " + str.substring((comand.length() + 1), str.length());
            }
        }
        for (LinkedList<String> list : getArchive().values()) {
            if (comand.equalsIgnoreCase("GET-STATALE") && list.get(index).toUpperCase().equalsIgnoreCase("STATALE")) {
                msg += list.toString().replace(";", ", ")
                        .replace("[", "").replace("]", "") + "\n";
            } else if (comand.equalsIgnoreCase("GET-PARITARIA") && list.get(index).toUpperCase().equalsIgnoreCase("PARITARIA")) {
                msg += list.toString().replace(";", ", ")
                        .replace("[", "").replace("]", "") + "\n";
            }
            if (!comand.equalsIgnoreCase("GET-STATALE") && !comand.equalsIgnoreCase("GET-PARITARIA")) {
                if (str.substring(comand.length() + 1).contains(list.get(index).toUpperCase())) {
                    msg += list.toString().replace(";", ", ")
                            .replace("[", "").replace("]", "") + "\n";
                }
            }

        }
        return msg;
    }

    private String changeCode(String str) {
        String msgTmp = str.substring(0, 1);
        for (int i = 1; i < str.length(); i++) {
            String charTmp = str.substring(i, i + 1);
            if (charTmp.equalsIgnoreCase("")) {
                msgTmp = msgTmp.substring(0, msgTmp.length() - 1);
            } else {
                msgTmp += charTmp;
            }
        }
        return msgTmp;
    }

    private String codes() {
        return "Codici possibili: END, STOP, GET-COMUNE, GET-PROVINCIA, GET-REGIONE, GET-CODICE, GET-ISTITUTO, GET-TIPOLOGIA-ISTITUTO, GET-INDIRIZZO, GET-CODICE-POSTALE, GET-TELEFONO, GET-FAX, GET-EMAIL, GET-EMAIL-PEC, GET-SITE, GET-DIREZIONE, GET-STATALE, GET-PARITARIA";
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
