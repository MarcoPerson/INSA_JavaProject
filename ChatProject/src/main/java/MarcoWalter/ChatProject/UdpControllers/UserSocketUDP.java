package MarcoWalter.ChatProject.UdpControllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import MarcoWalter.ChatProject.App;
import MarcoWalter.ChatProject.ControllerManager;
import MarcoWalter.ChatProject.HomeController;
import MarcoWalter.ChatProject.MessageController;
import MarcoWalter.ChatProject.Models.GroupData;
import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class UserSocketUDP {
	private User user;
	private DatagramSocket socketUDP;
	public static Map<InetAddress, Thread> threadMap = new HashMap<>();

	public UserSocketUDP(User _user) {
		user = _user;
		try {
			socketUDP = new DatagramSocket(2504);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void broadcast(int _id, String _pseudo, String _message) {
		try {
			int port = 2504;
			byte[] message = new byte[2000];
			message = String.valueOf(_id).concat("::").concat(_pseudo).concat("::").concat(_message).getBytes();

			ArrayList<InetAddress> tabBroadcast = new ArrayList<>();
			try {
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					NetworkInterface ni = interfaces.nextElement();
					if (!ni.isUp() || ni.isLoopback())
						continue;
					for (InterfaceAddress addr : ni.getInterfaceAddresses()) {
						InetAddress broadcast = addr.getBroadcast();
						if (broadcast != null) {
							tabBroadcast.add(broadcast);
							System.out.println("Broadcast address: " + broadcast.getHostAddress());
						}
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			}

			// Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			// NetworkInterface ni = en.nextElement();
			// InterfaceAddress ia = ni.getInterfaceAddresses().get(1);
			// Iterator<InterfaceAddress> networkInterface =
			// NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInterfaceAddresses().iterator();
			// InetAddress ipAddress = networkInterface.next().getBroadcast();

			InetAddress ipAddress = tabBroadcast.get(tabBroadcast.size() - 1);
			DatagramPacket packet = new DatagramPacket(message, message.length, ipAddress, port);
			socketUDP.send(packet);

			try {
				socketUDP.setSoTimeout(9000000);
			} catch (Exception e) {
			}

			System.out.println("Broadcast = " + ipAddress.getHostAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendMessage(OnlineUser _user, int _id, String _pseudo, String _message) throws IOException {
		byte[] message = new byte[2000];
		message = String.valueOf(_id).concat("::").concat(_pseudo).concat("::").concat(_message).getBytes();
		DatagramPacket packet = new DatagramPacket(message, message.length, _user.getIpAddress(), _user.getPort());
		socketUDP.send(packet);
		System.out.println("Message $" + String.valueOf(_id).concat("::").concat(_pseudo).concat("::").concat(_message)
				+ "$ was sent to " + _user.getId());
	}

	public boolean waitForAggrement() {
		boolean agreed = true;
		try {
			long time = System.currentTimeMillis();
			socketUDP.setSoTimeout(500);
			byte[] message = new byte[2000];
			DatagramPacket packet = new DatagramPacket(message, message.length);
			while (System.currentTimeMillis() - time < 2000) {
				System.out.println("Waiting for Agreement");
				socketUDP.receive(packet);
				String[] data = new String(packet.getData(), 0, packet.getLength()).split("::");

				if (Integer.parseInt(data[0]) == user.getId())
					continue;

				System.out.println("Message reçu = " + Arrays.toString(data));
				if (data[2].compareTo("Ok") == 0 || data[2].compareTo("Not Ok") == 0) {
					if (data[2].compareTo("Ok") != 0)
						agreed = false;
					user.getUserBookManager().addOnlineUser(Integer.parseInt(data[0]),
							new OnlineUser(data[1], Integer.parseInt(data[0]), packet.getAddress(), packet.getPort()));
				} else {
					agreed = true;
					if (data[2].compareTo("Not Ok Pseudo") == 0) {
						try {
							socketUDP.setSoTimeout(9000000);
						} catch (Exception e) {
						}
						return false;
					}
				}

			}
		} catch (Exception e) {
			System.out.println("Socket Time Out");
		}

		try {
			socketUDP.setSoTimeout(9000000);
		} catch (Exception e) {
		}
		System.out.println("Number of Online User : " + user.getUserBookManager().getUserBook().size());

		// Print all the Online Users
		// for (OnlineUser b : user.getUserBookManager().getUserBook().values()) {
		// System.out.println(b);
		// }

		if (!agreed) {
			user.getUserBookManager().deleteAllOnlineUser();
		}
		return agreed;
	}

	public static InetAddress getRandomMulticastAddress() throws Exception {

		int multicastOctet1 = new Random().nextInt(16) + 224;
		int multicastOctet2 = new Random().nextInt(256);
		int multicastOctet3 = new Random().nextInt(256);
		int multicastOctet4 = new Random().nextInt(256);
		String multicastAddressString = multicastOctet1 + "." + multicastOctet2 + "." + multicastOctet3 + "."
				+ multicastOctet4;
		return InetAddress.getByName(multicastAddressString);
	}

	public void receiveMessage() {
		try {
			byte[] message = new byte[2000];
			DatagramPacket packet = new DatagramPacket(message, message.length);
			while (true) {
				String replyMessage = "None";
				System.out.println("En attente de la reception des ...");
				try {
					socketUDP.setSoTimeout(9000000);
				} catch (Exception e) {
				}
				socketUDP.receive(packet);
				String[] data = new String(packet.getData(), 0, packet.getLength()).split("::");

				if (Integer.parseInt(data[0]) == user.getId())
					continue;

				System.out.println("Message reçu = " + Arrays.toString(data));
				OnlineUser newuser = new OnlineUser(data[1], Integer.parseInt(data[0]), packet.getAddress(),
						packet.getPort());
				if (data[2].equals("Connecting")) {
					if (data[1].equals(user.getPseudo())) {
						replyMessage = "Not Ok";
					} else {
						replyMessage = "Ok";
					}
				} else if (data[2].equals("newUser")) {
					if (user.getUserBookManager().getUserBook().containsKey(newuser.getId()))
						continue;
					user.getUserBookManager().addOnlineUser(newuser.getId(), newuser);
					HomeController.getInstance().items.add(newuser);
					System.out.println("Number of Online User : " + user.getUserBookManager().getUserBook().size());
					replyMessage = "userAdded";
				} else if (data[2].equals("Is Pseudo Ok")) {
					if (data[1].equals(user.getPseudo())) {
						replyMessage = "Not Ok Pseudo";
					} else {
						replyMessage = "Ok Pseudo";
					}
				} else if (data[2].equals("newPseudo")) {
					OnlineUser toChangePseudoUser = user.getUserBookManager().getUserBook().get(newuser.getId());
					String oldPseudo = toChangePseudoUser.getPseudo();
					toChangePseudoUser.setPseudo(newuser.getPseudo());
					replyMessage = "userPseudoChanged";

					// Rafraichir la liste des OnlineUsers
					new ControllerManager().updateTableList(HomeController.getInstance());
					if (App.discussionScenes.containsKey(newuser.getId())) {
						new ControllerManager().updateOnlineUser(App.discussionControllers.get(newuser.getId()),
								toChangePseudoUser);
						new ControllerManager().showNotification(HomeController.getInstance(),
								oldPseudo + " changes his pseudo to " + toChangePseudoUser.getPseudo());
					}
				} else if (data[2].equals("NewGroup")) {
					user.getUserBookManager().getusedMulticastAddress().add(InetAddress.getByName(data[3]));
					System.out.println("Bien Recu");
				} else if (data[2].equals("JoinTheChat")) {
					String groupName = data[3];
					InetAddress groupeIP = InetAddress.getByName(data[4]);
					int multicastPort = Integer.parseInt(data[5]);

					if (!user.getUserBookManager().getGroupBook().containsKey(groupeIP)) {
						FXMLLoader messageLoader = new FXMLLoader(App.class.getResource("message.fxml"));
						AnchorPane load = messageLoader.load();
						MessageController controller = messageLoader.getController();
						App.discussionGroupScenes.put(groupeIP, load);
						App.discussionGroupControllers.put(groupeIP, controller);

						controller.setGroupName(groupName);
						controller.setGroupeIP(groupeIP);
						controller.setMulticastPort(multicastPort);
						controller.setMySelf(user);
						controller.setUserPseudoText(groupName);

						new ControllerManager().setDiscussionScene(HomeController.getInstance(),
								App.discussionGroupScenes.get(groupeIP));
						new ControllerManager().setSendButtonAction(controller);

						// new MulticastSender(groupeIP, multicastPort, groupName,user);
						UserSocketUDP.threadMap.put(groupeIP,
								new MulticastReciever(groupeIP, multicastPort, groupName, user));

						GroupData newGroupData = new GroupData(groupName, groupeIP, multicastPort);
						user.getUserBookManager().addGroupData(groupeIP, newGroupData);
						HomeController.getInstance().groupItems.add(newGroupData);
					}

				} else if (data[2].equals("Disconnecting")) {
					new ControllerManager().manageDisconnectedUser(HomeController.getInstance(), newuser);
				}
				if (!replyMessage.equals("None"))
					sendMessage(newuser, user.getId(), user.getPseudo(), replyMessage);
			}
		} catch (SocketException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			socketUDP.close();
		} catch (Exception e) {
		}
	}
}
