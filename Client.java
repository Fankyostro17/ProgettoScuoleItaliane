import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread{
    private InetAddress indirizzo;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static final int PORT = 1717;

    public Client(InetAddress indirizzo) {
        setIndirizzo(indirizzo);
        try {
            setSocket(new Socket(indirizzo, PORT));
        } catch (Exception e) {
            
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Connection accepted: " + getSocket());
            setOut(new PrintWriter(new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream())), true));
            setIn(new BufferedReader(new InputStreamReader(getSocket().getInputStream())));
            getOut().println("Benvenuto nel Progetto Scuole Italiane! - Client");
            getOut().flush();

            while (true) {
                getOut().print("Insert Code -> ");
                String str = getIn().readLine();
                if (str.equals("END") || str.equals("STOP")) {
                    break;
                }
            }

            getSocket().close();
            getIn().close();
            getOut().close();
        } catch (Exception e) {
            
        }
    }

    public InetAddress getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(InetAddress indirizzo) {
        this.indirizzo = indirizzo;
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
    
}
