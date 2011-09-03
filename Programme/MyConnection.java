import java.io.*;
import java.net.*;

public class MyConnection {
    Socket s;
    InputStream istream;
    InputStreamReader isr;
    BufferedReader in;
    OutputStream ostream;
    OutputStreamWriter osw;
    PrintWriter out;
    int connectionID;
    String clientName;
    String status;

    public MyConnection(Socket s) {
        try {
            this.s = s;
            this.istream = s.getInputStream();
            this.isr = new InputStreamReader(istream);
            this.in = new BufferedReader(isr);
            this.ostream = s.getOutputStream();
            this.osw = new OutputStreamWriter(ostream);
            this.out = new PrintWriter(osw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String msg) {
        out.println(msg);
        out.flush();
        return true;
    }

    public String getMessage() {
        try {
            String msg = in.readLine();
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getConnectionID(){
        return connectionID;
    }

    public void setConnectionID(int connectionID){
        this.connectionID = connectionID;
    }

    public String getClientName(){
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
