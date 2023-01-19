package MarcoWalter.ChatProject.Models;

import java.net.InetAddress;


public class GroupData{
    private String groupName;
    private InetAddress ipAddress;
    private int port;
    
    public GroupData (String _groupName, InetAddress _ipAddress, int _port){
    	groupName = _groupName;
        ipAddress = _ipAddress;
        port = _port;
    }
    public GroupData (InetAddress _ipAddress, int _port){
        ipAddress = _ipAddress;
        port = _port;
    }

    public String getGroupName(){
        return groupName;
    }

    public InetAddress getIpAddress(){
        return ipAddress;
    }
    public int getPort(){
        return port;
    }

    public void setGroupName(String _groupName){
    	groupName = _groupName;
    }

    public void setIpAddress(InetAddress _ipAddress){
        ipAddress = _ipAddress;
    }
    public void setPort(int _port){
        port = _port;
    }
    public String toString(){
        return "Group with name " + groupName + ", ipAdress " + ipAddress.getHostAddress() + " and Port " + port;
    }
}