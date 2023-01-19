package MarcoWalter.ChatProject.Models;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import MarcoWalter.ChatProject.App;
import MarcoWalter.ChatProject.ControllerManager;
import MarcoWalter.ChatProject.HomeController;
import MarcoWalter.ChatProject.MessageController;
import MarcoWalter.ChatProject.TcpControllers.ThreadMessageReceiver;
import MarcoWalter.ChatProject.TcpControllers.UserSocketClient;
import MarcoWalter.ChatProject.TcpControllers.UserSocketTCP;
import MarcoWalter.ChatProject.UdpControllers.MulticastReciever;
import MarcoWalter.ChatProject.UdpControllers.MulticastSender;
import MarcoWalter.ChatProject.UdpControllers.UserSocketUDP;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class User {
    private String pseudo;
    private int id ;
    private UserBookManager userBookManager;

    public User(int _id){
        id = _id ;
        userBookManager = new UserBookManager();
    }
    
    public String getPseudo(){
        return pseudo;
    }

    public int getId(){
        return id;
    }

    public void modifyPseudo(String _pseudo){
        pseudo = _pseudo;
    }

    public UserBookManager getUserBookManager(){
        return userBookManager;
    }
    
    public boolean connectToNetwork(UserSocketUDP socketUDP){
        boolean agreed = false;
        try {
            System.out.println(pseudo);
            socketUDP.broadcast(id, pseudo, "Connecting");
            //agreed = socketUDP.waitForAggrement();
        } catch (Exception e) {
            System.out.println("Error Connect : " + e.getMessage());
        }
        return agreed;
    }
    
    public boolean disconnectectFromNetwork(UserSocketUDP socketUDP){
        boolean agreed = false;
        try {
            socketUDP.broadcast(id, pseudo, "Disconnecting");
            userBookManager.deleteAllOnlineUser();
        } catch (Exception e) {
        }
        return agreed;
    }
    
    public void createChatGroup(String groupName, ArrayList <OnlineUser> memberList,UserSocketUDP socketUDP){
        ArrayList <InetAddress> usedMulticastAddressses = this.userBookManager.getusedMulticastAddress();
        InetAddress groupeIP;
        try {
        	do{
        		groupeIP = UserSocketUDP.getRandomMulticastAddress();
        	}while(usedMulticastAddressses.contains(groupeIP));
        	
        	String _groupeIP = groupeIP.toString().split("/")[1];
        	int multicastPort = UserSocketClient.findFreePort(); 
        	String _message = "NewGroup".concat("::").concat(_groupeIP);
        	String message = "JoinTheChat".concat("::").concat(groupName).concat("::").concat(_groupeIP).concat("::").concat(Integer.toString(multicastPort));
        	socketUDP.broadcast(this.getId(), this.getPseudo(), _message);
        	
        	for (OnlineUser user : memberList ){
        		socketUDP.sendMessage(user, this.getId(), this.getPseudo(), message);
        	}
        	
        	FXMLLoader messageLoader = new FXMLLoader(App.class.getResource("message.fxml"));
            AnchorPane load = messageLoader.load();
            MessageController controller = messageLoader.getController();
            App.discussionGroupScenes.put(groupeIP, load);
            App.discussionGroupControllers.put(groupeIP, controller);


            controller.setGroupName(groupName);
            controller.setGroupeIP(groupeIP);
            controller.setMulticastPort(multicastPort);
            controller.setMySelf(this);
            controller.setUserPseudoText(groupName);
            
            new ControllerManager().setDiscussionScene(HomeController.getInstance(), App.discussionGroupScenes.get(groupeIP));
            new ControllerManager().setSendButtonAction(controller);
            
//        	new MulticastSender(groupeIP, multicastPort, groupName,this);
            UserSocketUDP.threadMap.put(groupeIP, new MulticastReciever(groupeIP, multicastPort, groupName, this));
            
            GroupData newGroupData = new GroupData(groupName ,groupeIP, multicastPort);
            this.getUserBookManager().addGroupData(groupeIP, newGroupData);
			HomeController.getInstance().groupItems.add(newGroupData);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}