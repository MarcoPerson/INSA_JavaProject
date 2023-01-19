package MarcoWalter.ChatProject.UdpControllers;

import MarcoWalter.ChatProject.Models.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender extends Thread {
	InetAddress groupeIP;
	int port;
	MulticastSocket socketEmission;
	String nom;
	User user;
	String message;

	public MulticastSender(InetAddress _groupeIP, int _port, String _nom, User _user, String _message) {
		groupeIP = _groupeIP;
		port = _port;
		nom = _nom;
		user = _user;
		message = _message;
		try {
			socketEmission = new MulticastSocket();
			socketEmission.setTimeToLive(1); // pour un site
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String texte = nom.concat("::").concat(message).concat("::").concat(user.getPseudo());
			emettre(texte);
		} catch (Exception exc) {
			System.out.println(exc);
		}
	}

	void emettre(String texte) throws Exception {
		byte[] contenuMessage;
		DatagramPacket message;

		ByteArrayOutputStream sortie = new ByteArrayOutputStream();
		(new DataOutputStream(sortie)).writeUTF(texte);
		contenuMessage = sortie.toByteArray();
		message = new DatagramPacket(contenuMessage, contenuMessage.length, groupeIP, port);
		socketEmission.send(message);
		sortie.close();
		socketEmission.close();
	}
}