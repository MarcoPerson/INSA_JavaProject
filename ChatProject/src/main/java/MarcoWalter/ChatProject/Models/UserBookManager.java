package MarcoWalter.ChatProject.Models;

import java.util.HashMap;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;

public class UserBookManager {
	private HashMap<Integer, OnlineUser> onlineUserBook;
	private ArrayList<InetAddress> usedMulticastAddress;

	public UserBookManager() {
		onlineUserBook = new HashMap<>();
		usedMulticastAddress = new ArrayList<InetAddress>();
	}

	public HashMap<Integer, OnlineUser> getUserBook() {
		return onlineUserBook;
	}

	public void addOnlineUser(int _id, OnlineUser _user) {
		onlineUserBook.put(_id, _user);
	}

	public OnlineUser chooseOnlineUser(int id) {
		return onlineUserBook.get(id);
	}

	public void removeOnlineUser(int _id) {
		onlineUserBook.remove(_id);
	}

	public void modifyOnlineUser(int _id, String _pseudo) {
		OnlineUser onlineUser = onlineUserBook.get(_id);
		onlineUser.setPseudo(_pseudo);
		onlineUserBook.put(_id, onlineUser);
	}

	public void deleteAllOnlineUser() {
		onlineUserBook.clear();
	}

	public ArrayList<InetAddress> getusedMulticastAddress() {
		return usedMulticastAddress;
	}

	public ArrayList<OnlineUser> getListOfOnlineUser() {
		ArrayList<OnlineUser> values = new ArrayList<>(this.getUserBook().values());
		return values;
	}
}