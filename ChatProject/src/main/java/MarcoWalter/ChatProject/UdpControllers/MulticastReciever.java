package MarcoWalter.ChatProject.UdpControllers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReciever extends Thread {
    InetAddress  groupeIP;
    int port;
    String nom;
    MulticastSocket socketReception;
    
 
    public MulticastReciever(InetAddress _groupeIP, int _port, String _nom)  throws Exception { 
        groupeIP = _groupeIP;
        port = _port;
        nom = _nom;
        socketReception = new MulticastSocket(port);
        socketReception.joinGroup(groupeIP);
        start();
   }
 
   public void run() {
     DatagramPacket message;
     byte[] contenuMessage;
     String received;
     ByteArrayInputStream lecteur;
     
     while(true) {
           contenuMessage = new byte[1024];
           message = new DatagramPacket(contenuMessage, contenuMessage.length);
           try {
                   socketReception.receive(message);
                   received = (new DataInputStream(new ByteArrayInputStream(contenuMessage))).readUTF();
            if (!received.startsWith(nom)) continue;
            String [] data = received.split("::");
            received ="[" + data[0] + "]" + data[2] + " :" + data[1];
            System.out.println(received);
           }
           catch(Exception exc) {
                 System.out.println(exc);
           }
     }
   }
 }