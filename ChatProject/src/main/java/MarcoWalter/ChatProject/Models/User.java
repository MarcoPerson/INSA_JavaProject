package MarcoWalter.ChatProject.Models;

import java.util.HashMap;
import MarcoWalter.ChatProject.UdpControllers.UserSocketUDP;

public class User {
    private String pseudo;
    private int id ;
    private UserBookManager userBookManager;
    private HashMap <Integer,String> dataBase;

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
    
}