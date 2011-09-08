import java.net.*;

public class Server {
	public static void main(String args[]) {
		try {
			ServerSocket ssocket = new ServerSocket(8888);
			while (true) {
				System.out.println("Server: Waiting for connections...");
				Socket s = ssocket.accept();
				String clientip = s.getInetAddress().toString();
				System.out.println("Server: " + clientip + " connected!");
				MyConnection conn = new MyConnection(s);
				MessengerThread at = new MessengerThread(conn, s);
				at.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
