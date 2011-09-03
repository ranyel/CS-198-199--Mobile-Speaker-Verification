import java.net.*;
import javax.swing.*;
import java.util.*;

public class MyClient extends JDialog {
    static String msg = "";
    static Socket s;
    static MyConnection conn;
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    public static JTextArea messagesWindow;
    private static JTextArea onlineClients;
    private static JTextArea textField;

    /** Creates new form ClientInterface */
    public MyClient(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        conn.sendMessage("/conn");  //notify others that this client is now connected
    }

    /** Initialise the client user interface */
    private void initComponents() {

        jLabel1 = new JLabel();
        jScrollPane1 = new JScrollPane();
        messagesWindow = new JTextArea();
        jButton1 = new JButton();
        jLabel2 = new JLabel();
        jScrollPane3 = new JScrollPane();
        textField = new JTextArea();
        jScrollPane4 = new JScrollPane();
        onlineClients = new JTextArea();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CS 145 MP 1 Client");

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel1.setText("Chat Window");

        messagesWindow.setEditable(false);
        jScrollPane1.setViewportView(messagesWindow);

        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel2.setText("Online Clients");

        textField.setColumns(20);
        jScrollPane3.setViewportView(textField);

        onlineClients.setEditable(false);
        jScrollPane4.setViewportView(onlineClients);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(303, 303, 303))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jButton1, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addComponent(jScrollPane4, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        msg = this.textField.getText();
        conn.sendMessage(msg);
        this.textField.setText("");
    }


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MyClient dialog = new MyClient(new JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
        try {
            s = new Socket("127.0.0.1", 8888);
            conn = new MyConnection(s);
            ClientReceiverThread receiver = new ClientReceiverThread(conn);
            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Dedicated thread for receiving messages */
    public static class ClientReceiverThread extends Thread {
        MyConnection conn;

        public ClientReceiverThread(MyConnection conn) {
            this.conn = conn;
        }

        public void run() {
            while(true) {
                String msg = conn.getMessage();
                if (msg.equals("Goodbye")) {
                    System.exit(0);
                }
                else if (msg.endsWith("connected!")) {
                    messagesWindow.append(msg + "\n");
                    StringTokenizer token = new StringTokenizer(msg);
                    token.nextToken();
                    String name = token.nextToken();
                    onlineClients.append(name + "\n");
                }
                else if (msg.startsWith("$")) {
                    onlineClients.setText("");
                    StringTokenizer token = new StringTokenizer(msg, "$");
                    while (token.hasMoreElements()) {
                        onlineClients.append(token.nextToken() + "\n");
                    }
                }
                else {
                    messagesWindow.append(msg + "\n");
                }
            }
        }
    }
}
