
public class ClientReceiverThread extends Thread {
	MyConnection conn;
	
	public ClientReceiverThread(MyConnection conn) {
		this.conn = conn;
	}
	
	public void run() {
		while (true) {
		    String message = conn.getMessage();
			System.out.println("Server: " + message);
			if (message.equals("Goodbye")) {
			    break;
			}
		}
	}
}
