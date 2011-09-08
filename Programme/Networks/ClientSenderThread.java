import javax.swing.*;
public class ClientSenderThread extends Thread {
	MyConnection conn;
	
	public ClientSenderThread(MyConnection conn) {
		this.conn = conn;
	}
	
	public void run() {
		while(true) {
			String msg = JOptionPane.showInputDialog("Enter message");
			conn.sendMessage(msg);
			if(msg.equals("QUIT")){
				break;
			}
		}
	}
}