import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MessengerThread extends Thread {
	MyConnection conn;
	Socket s;
	
	public MessengerThread(MyConnection conn, Socket s) {
		this.conn = conn;
		this.s = s;
	}
	
	public void run() {
		try {
			while (true) {
				String msg = conn.getMessage();
				if (msg.equals("QUIT")) {
					conn.sendMessage("Goodbye");
					try {
						s.close();
					} catch (Exception e) {
						e.printStackTrace();
					}						
					break;
				}
				else if (msg.equals("TIME")) {
					DateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy");
					Date date = new Date();
					conn.sendMessage("The time is now " + dateFormat.format(date));
				}
				else if (msg.startsWith("MY NAME IS")) {
					conn.sendMessage("Hello, " + msg.substring(11));
				}					
				else if (msg.equals("JOKE TIME")) {
					conn.sendMessage("Knock knock");
					if (conn.getMessage().equalsIgnoreCase("Who's there?")) {
						conn.sendMessage("SAFARI");
						if (conn.getMessage().equalsIgnoreCase("Safari who?")) {
							conn.sendMessage("Got my hands up, they're playing my song and now I'm gonna be okay. It's SAFARI in the U.S.A.");
						}
						else {
							conn.sendMessage("I don't understand what you're saying");
						}
					}
					else {
						conn.sendMessage("I don't understand what you're saying");
					}
				}
				else {
					conn.sendMessage("I can't understand what you're saying");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
