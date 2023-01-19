package MarcoWalter.ChatProject.UdpControllers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import MarcoWalter.ChatProject.App;
import MarcoWalter.ChatProject.ControllerManager;
import MarcoWalter.ChatProject.MessageController;
import MarcoWalter.ChatProject.Models.User;

public class MulticastReciever extends Thread {
	InetAddress groupeIP;
	int port;
	String nom;
	User user;
	MulticastSocket socketReception;

	public MulticastReciever(InetAddress _groupeIP, int _port, String _nom, User _user) throws Exception {
		groupeIP = _groupeIP;
		port = _port;
		nom = _nom;
		user = _user;
		socketReception = new MulticastSocket(port);
		socketReception.joinGroup(groupeIP);
		start();
	}

	public void run() {
		DatagramPacket message;
		byte[] contenuMessage;
		String received;

		while (true) {
			contenuMessage = new byte[2000];
			message = new DatagramPacket(contenuMessage, contenuMessage.length);
			try {
				socketReception.receive(message);
				received = (new DataInputStream(new ByteArrayInputStream(contenuMessage))).readUTF();
				String[] data = received.split("::");
				received = "[" + data[0] + "]" + data[2] + " :" + data[1];
				if (data[2].equals(user.getPseudo()))
					continue;

				String pattern = "MM/dd/yyyy HH:mm:ss";
				DateFormat df = new SimpleDateFormat(pattern);
				Date today = Calendar.getInstance().getTime();
				String todayAsString = df.format(today);

				MessageController controller = App.discussionGroupControllers.get(groupeIP);
				new ControllerManager().addMessageIntoScrollPane(controller, data[1] + "::" + data[2], todayAsString,
						"group");

				System.out.println(received);
			} catch (Exception exc) {
				System.out.println(exc);
			}
		}
	}
}