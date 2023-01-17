package MarcoWalter.ChatProject.Models;

import java.net.InetAddress;
import java.util.ArrayList;

import MarcoWalter.ChatProject.TcpControllers.UserSocketClient;
import MarcoWalter.ChatProject.UdpControllers.MulticastReciever;
import MarcoWalter.ChatProject.UdpControllers.MulticastSender;
import MarcoWalter.ChatProject.UdpControllers.UserSocketUDP;

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
    
    public void createChatGroup(String groupName, ArrayList <OnlineUser> memberList,UserSocketUDP socketUDP) throws Exception{
        ArrayList <InetAddress> usedMulticastAddressses = this.userBookManager.getusedMulticastAddress();
        InetAddress groupeIP;
        do{
               groupeIP = UserSocketUDP.getRandomMulticastAddress();
        }while(usedMulticastAddressses.contains(groupeIP));

        String _groupeIP = groupeIP.toString();
        int multicastPort = UserSocketClient.findFreePort(); 
        String _message = "NewGroup".concat("::").concat(_groupeIP);
        String message = "JoinTheChat".concat("::").concat(groupName).concat("::").concat(_groupeIP).concat("::").concat(Integer.toString(multicastPort));
        socketUDP.broadcast(this.getId(), this.getPseudo(), _message);

        for (OnlineUser user : memberList ){
            socketUDP.sendMessage(user, this.getId(), this.getPseudo(), message);
        }

        new MulticastSender(groupeIP, multicastPort, groupName,this);
        new MulticastReciever(groupeIP, multicastPort, groupName);
    }
    
}