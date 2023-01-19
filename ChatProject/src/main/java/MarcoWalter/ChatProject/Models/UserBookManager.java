package MarcoWalter.ChatProject.Models;

import java.util.HashMap;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;

public class UserBookManager {
	private HashMap<Integer, OnlineUser> onlineUserBook;
	private ArrayList<InetAddress> usedMulticastAddress;
	private HashMap<InetAddress, GroupData> groupDataBook;

	public UserBookManager() {
		onlineUserBook = new HashMap<>();
		usedMulticastAddress = new ArrayList<InetAddress>();
		groupDataBook = new HashMap<>();
	}

	public HashMap<Integer, OnlineUser> getUserBook() {
		return onlineUserBook;
	}
	
	public HashMap<InetAddress, GroupData> getGroupBook() {
		return groupDataBook;
	}

	public void addOnlineUser(int _id, OnlineUser _user) {
		onlineUserBook.put(_id, _user);
	}
	
	public void addGroupData(InetAddress _idAddress, GroupData _group) {
		groupDataBook.put(_idAddress, _group);
	}

	public OnlineUser chooseOnlineUser(int id) {
		return onlineUserBook.get(id);
	}
	
	public GroupData chooseGroupData(InetAddress _idAddress) {
		return groupDataBook.get(_idAddress);
	}

	public void removeOnlineUser(int _id) {
		onlineUserBook.remove(_id);
	}
	
	public void removeGroupData(InetAddress _idAddress) {
		groupDataBook.remove(_idAddress);
	}

	public void modifyOnlineUser(int _id, String _pseudo) {
		OnlineUser onlineUser = onlineUserBook.get(_id);
		onlineUser.setPseudo(_pseudo);
		onlineUserBook.put(_id, onlineUser);
	}
	
	public void modifyGroupData(InetAddress _idAddress, GroupData _data) {
		groupDataBook.put(_idAddress, _data);
	}

	public void deleteAllOnlineUser() {
		onlineUserBook.clear();
	}
	
	public void deleteAllGroupData() {
		groupDataBook.clear();
	}

	public ArrayList<InetAddress> getusedMulticastAddress() {
		return usedMulticastAddress;
	}

	public ArrayList<OnlineUser> getListOfOnlineUser() {
		ArrayList<OnlineUser> values = new ArrayList<>(this.getUserBook().values());
		return values;
	}
}