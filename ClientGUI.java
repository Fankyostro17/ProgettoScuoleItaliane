
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.*;

public class ClientGUI extends JFrame implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner consoleScanner;

    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;

    public static final int PORT = 1717;

    public ClientGUI(String serverAddress) {
        setTitle("Client GUI");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setTextArea(new JTextArea());
        getTextArea().setEditable(false);
        JScrollPane scrollPane = new JScrollPane(getTextArea());

        setTextField(new JTextField());
        setSendButton(new JButton("Send"));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        panel.add(getTextField(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        panel.add(getSendButton(), gbc);

        add(panel);

        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        };

        getSendButton().addActionListener(sendAction);
        getTextField().addActionListener(sendAction);

        connectToServer(serverAddress);
    }

    private void sendMessage() {
        String msg = getTextField().getText();
        if (!msg.isEmpty()) {
            if (msg.equalsIgnoreCase("END")) {
                closeGUI();
                closeConnection();
                return;
            } else if (msg.equalsIgnoreCase("STOP")) {
                getOut().println(msg);
                getOut().flush();
                closeGUI();
                closeConnection();
                return;
            }
            getOut().println(msg);
            getOut().flush();
            getTextField().setText("");
        }
    }

    private void closeGUI() {
        dispose();
    }

    private void closeConnection() {
        try {
            getIn().close();
            getOut().close();
            getSocket().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while (!(line = getIn().readLine()).startsWith("Insert Code ->")) {
                appendText(line + "\n");
            }
            appendText(line + "\n");

            while (true) {
                line = getIn().readLine();
                if (line == null) {
                    break;
                }
                appendText(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectToServer(String serverAddress) {
        try {
            setSocket(new Socket(serverAddress, PORT));
            System.out.println("Connection accepted: " + getSocket());

            setOut(new PrintWriter(new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream())), true));
            setIn(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            setConsoleScanner(new Scanner(System.in));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> getTextArea().append(text));
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

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public static void main(String[] args) throws IOException {
        String serverAddress = "127.0.0.1";
        SwingUtilities.invokeLater(() -> {
            ClientGUI clientGUI = new ClientGUI(serverAddress);
            clientGUI.setVisible(true);
            Thread clientThread = new Thread(clientGUI);
            clientThread.start();
        });
    }
}
