package MarcoWalter.ChatProject.UdpControllers;

import java.io.BufferedReader;
import MarcoWalter.ChatProject.Models.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender extends Thread{
    InetAddress  groupeIP;
    int port;
    MulticastSocket socketEmission;
    String nom;
    User user;
   
    public MulticastSender(InetAddress _groupeIP, int _port, String _nom, User _user) throws Exception {
       groupeIP = _groupeIP;
       port = _port;
       nom = _nom;
       user = _user;
       socketEmission = new MulticastSocket();
       socketEmission.setTimeToLive(1); // pour un site
       start();
   }
     
   public void run() {
     BufferedReader entreeClavier;
     
     try {
        entreeClavier = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
               String entreeclavier = entreeClavier.readLine();
               String texte = nom.concat("::").concat(entreeclavier).concat("::").concat(user.getPseudo());
               emettre(texte);
        }
     }
     catch (Exception exc) {
        System.out.println(exc);
     }
   } 
 
   void emettre(String texte) throws Exception {
         byte[] contenuMessage;
         DatagramPacket message;
     
         ByteArrayOutputStream sortie = new ByteArrayOutputStream(); 
         (new DataOutputStream(sortie)).writeUTF(texte); 
         contenuMessage = sortie.toByteArray();
         message = new DatagramPacket(contenuMessage, contenuMessage.length, groupeIP, port);
         socketEmission.send(message);
   }
 }