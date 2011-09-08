import java.net.*;
import javax.swing.*;

public class Client {
	public static void main(String args[]) {
		try {
			Socket s = new Socket("10.0.1.54", 8888);			
			MyConnection conn = new MyConnection(s);
			ClientReceiverThread receiver = new ClientReceiverThread(conn);
			ClientSenderThread sender = new ClientSenderThread(conn);
			receiver.start();
			sender.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
