import java.net.*;
import java.util.*;

public class MyServer {
    public static void main(String[] args) {
        ArrayList <MyConnection> arrList = new ArrayList();
        int connectedClients = 1;
        String clientName;
        try {
            ServerSocket ssocket = new ServerSocket(8888);
            while(true) {
                System.out.println("Server: waiting for connections...");
                Socket s = ssocket.accept();
                String clientIP = s.getInetAddress().toString();
                System.out.println("Server: " + clientIP + " connected!");
                MyConnection conn = new MyConnection(s);
                conn.setConnectionID(connectedClients);
                clientName = "Client" + connectedClients;
                conn.setClientName(clientName);
                conn.setStatus("");

                connectedClients++;
                Messenger mt = new Messenger(conn, s, arrList);
                mt.start();
                arrList.add(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Messenger extends Thread {
        MyConnection conn;
        Socket s;
        ArrayList <MyConnection> arrList;

        public Messenger (MyConnection conn, Socket s, ArrayList arrList) {
            this.conn = conn;
            this.s = s;
            this.arrList = arrList;
        }

        public void run() {
            try {
                while(true) {
                    String msg = conn.getMessage();
                    if (msg.startsWith("/")) {
                        if (msg.startsWith("/changename")) {
                            String prevName = conn.getClientName();
                            String newName = msg.substring(11).trim();
                            if (newName.equals("")) {
                                conn.sendMessage("Server: [Error] Usage: /changename <name>");
                            }
                            else if(newName.contains(" ")) {
                                conn.sendMessage("Server: [Error] Usernames should consist of only one word.");
                            }
                            else {
                                for (int i = 0; i < arrList.size(); i++) {
                                    if(newName.equals(arrList.get(i).getClientName())) {
                                        conn.sendMessage("Server: [Error] Username already exists.");
                                        newName = prevName;
                                        break;
                                    }
                                }
                                conn.setClientName(newName);
                                if (!newName.equals(prevName)) {
                                    sendToAll("Server message", prevName + " has changed name to " + conn.getClientName());
                                    update();
                                }
                            }
                        }
                        else if (msg.startsWith("/changestatus")) {
                            String newStatus = msg.substring(13).trim();
                            conn.setStatus(newStatus);
                            sendToAll("Server message", conn.getClientName() + " has changed status to " + "\"" + newStatus + "\"");
                            update();
                        }
                        else if (msg.startsWith("/whisper")) {
                            String content = msg.substring(8).trim();
                            int i = content.indexOf(" ");
                            String destUser = content.substring(0, i).trim();
                            String pm = content.substring(i).trim();
                            for (int j = 0; j < arrList.size(); j++) {
                                if(arrList.get(j).getClientName().equals(destUser)) {
                                    sendToSomeone(conn, arrList.get(j), pm);
                                    break;
                                }
                            }
                        }
                        else if (msg.equals("/quit")) {
                            conn.sendMessage("Goodbye");
                            sendToAll("Server message", conn.getClientName() + " has left");
                            try {
                                s.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            arrList.remove(conn);
                            update();
                            break;
                        }
                        else if (msg.equals("/conn")){
                            sendToAll("Server message", conn.getClientName()+" connected!");
                            update();
                        }
                        else {
                            conn.sendMessage("Server message: Invalid command" + msg);
                        }
                    }
                    else {
                        msg = msg.trim();
                        if (!msg.isEmpty()){
                            sendToAll(conn.getClientName(),msg.trim());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void sendToAll(String source, String msg) {
            int arrSize = arrList.size();
            int i;
            for (i = 0; i < arrSize; i++){
                arrList.get(i).sendMessage(source + ": " + msg);
            }
        }

        public void sendToSomeone(MyConnection source, MyConnection destination, String message) {
            destination.sendMessage("[" + source.getClientName() + " whispers" + "]: " + message);
            if (!destination.equals(source)) {
                source.sendMessage("[" + source.getClientName() + " whispers" + "]: " + message);
            }
        }

        public void update(){
            String updt = "";
            for (int i = 0; i < arrList.size(); i++) {
                updt = updt + "$" + arrList.get(i).getClientName() + " - " +  arrList.get(i).getStatus();
            }
            for (int i = 0; i < arrList.size(); i++) {
                arrList.get(i).sendMessage(updt);
            }

        }
    }
}
