package MarcoWalter.ChatProject.Models;

import java.util.HashMap;

public class UserBookManager{
    private HashMap <Integer,OnlineUser> onlineUserBook;

    public UserBookManager(){
        onlineUserBook = new HashMap<>();
    }

    public HashMap <Integer,OnlineUser> getUserBook(){
        return onlineUserBook;
    }

    public void addOnlineUser(int _id, OnlineUser _user){
        onlineUserBook.put(_id, _user);
    }

    public OnlineUser chooseOnlineUser(int id){
        return onlineUserBook.get(id);
    }


    public void removeOnlineUser (int _id){
        onlineUserBook.remove(_id);
    }

    public void modifyOnlineUser(int _id, String _pseudo){
        OnlineUser onlineUser = onlineUserBook.get(_id);
        onlineUser.setPseudo(_pseudo);
        onlineUserBook.put(_id, onlineUser);
    }
    
    public void deleteAllOnlineUser(){
        onlineUserBook.clear();
    }
}