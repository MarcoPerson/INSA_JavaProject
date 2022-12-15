package MarcoWalter.ChatProject.UdpControllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.User;

public class UserSocketUDP {
    private User user;
    private DatagramSocket socketUDP;

    public UserSocketUDP(User _user) {
        user = _user;
        try {
            socketUDP = new DatagramSocket(2504);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(int _id, String _pseudo, String _message) {
        try {
            int port = 2504;
            byte[] message = new byte[50];
            message = String.valueOf(_id).concat("::").concat(_pseudo).concat("::").concat(_message).getBytes();
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            NetworkInterface ni = en.nextElement();
            InterfaceAddress ia = ni.getInterfaceAddresses().get(1);
            InetAddress ipAddress = ia.getBroadcast();
            DatagramPacket packet = new DatagramPacket(message, message.length, ipAddress, port);
            socketUDP.send(packet);
            socketUDP.receive(packet);
            System.out.println(" Broadcast = " + ipAddress.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(OnlineUser _user, int _id, String _pseudo, String _message) throws IOException {
        byte[] message = new byte[50];
        message = String.valueOf(_id).concat("::").concat(_pseudo).concat("::").concat(_message).getBytes();
        DatagramPacket packet = new DatagramPacket(message, message.length, _user.getIpAddress(), _user.getPort());
        socketUDP.send(packet);
        System.out.println("Message $" + String.valueOf(_id).concat("::").concat(_pseudo).concat("::").concat(_message)
                + "$ was sent to " + _user.getId());
    }

    public boolean waitForAggrement() {
        boolean agreed = true;
        try {
            long time = System.currentTimeMillis();
            socketUDP.setSoTimeout(300);
            byte[] message = new byte[50];
            DatagramPacket packet = new DatagramPacket(message, message.length);
            while (System.currentTimeMillis() - time < 2000) {
                System.out.println("Waiting for Agreement");
                socketUDP.receive(packet);
                String[] data = new String(packet.getData(), 0, packet.getLength()).split("::");
                System.out.println("Message reçu = " + Arrays.toString(data));
                if (data[2].compareTo("Ok") != 0)
                    agreed = false;
                user.getUserBookManager().addOnlineUser(Integer.parseInt(data[0]),
                        new OnlineUser(data[1], Integer.parseInt(data[0]), packet.getAddress(), packet.getPort()));
            }
        } catch (Exception e) {
        }
        
        try {
        	socketUDP.setSoTimeout(9000000);
        }
        catch(Exception e) {}
        System.out.println("Number of Online User : " + user.getUserBookManager().getUserBook().size());

        // Print all the Online Users
        // for (OnlineUser b : user.getUserBookManager().getUserBook().values()) {
        // System.out.println(b);
        // }

        if (!agreed) {
            user.getUserBookManager().deleteAllOnlineUser();
        }
        return agreed;
    }

    public void receiveMessage() {
        try {
            byte[] message = new byte[50];
            String replyMessage = "None";
            DatagramPacket packet = new DatagramPacket(message, message.length);
            while (true) {
                System.out.println("En attente de la reception des ...");
                socketUDP.receive(packet);
                String[] data = new String(packet.getData(), 0, packet.getLength()).split("::");
                System.out.println("Message reçu = " + Arrays.toString(data));
                OnlineUser newuser = new OnlineUser(data[1], Integer.parseInt(data[0]), packet.getAddress(),
                        packet.getPort());
                if (data[2].equals("Connecting")) {
                    if (data[1].equals(user.getPseudo())) {
                        replyMessage = "Not Ok";
                    } else {
                        replyMessage = "Ok";
                    }
                } else if (data[2].equals("newUser")) {
                    user.getUserBookManager().addOnlineUser(newuser.getId(), newuser);
                    System.out.println("Number of Online User : " + user.getUserBookManager().getUserBook().size());
                    replyMessage = "userAdded";
                }
                if(!replyMessage.equals("None")) sendMessage(newuser, user.getId(), user.getPseudo(), replyMessage);
            }
        } catch (Exception e) {
        }
    }
}
