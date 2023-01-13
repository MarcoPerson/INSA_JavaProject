package MarcoWalter.ChatProject.Models;

import java.net.InetAddress;


public class OnlineUser{
    private String pseudo;
    private int id ;
    private InetAddress ipAddress;
    private int port;
    
    public OnlineUser (String _pseudo, int _id, InetAddress _ipAddress, int _port){
        pseudo = _pseudo;
        id = _id;
        ipAddress = _ipAddress;
        port = _port;
    }
    public OnlineUser (InetAddress _ipAddress, int _port){
        ipAddress = _ipAddress;
        port = _port;
    }

    public String getPseudo(){
        return pseudo;
    }
    public int getId(){
        return id;
    }
    public InetAddress getIpAddress(){
        return ipAddress;
    }
    public int getPort(){
        return port;
    }

    public void setPseudo(String _pseudo){
        pseudo = _pseudo;
    }
    public void setId(int _id){
        id  = _id;
    }
    public void setIpAddress(InetAddress _ipAddress){
        ipAddress = _ipAddress;
    }
    public void setPort(int _port){
        port = _port;
    }
    public String toString(){
        return "Online User with Pseudo " + pseudo + ", Id " + id + ", ipAdress " + ipAddress.getHostAddress() + " and Port " + port;
    }
}