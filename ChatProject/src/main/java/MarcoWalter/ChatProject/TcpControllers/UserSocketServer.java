package MarcoWalter.ChatProject.TcpControllers;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.User;


public class UserSocketServer extends UserSocketTCP {
    ServerSocket server;

    public UserSocketServer(User _user) {
    	super(_user);
        try {
            server = new ServerSocket(3101);
        } catch (Exception e) {
            System.out.println("ServerError : " + e.getMessage());
        }
    }

    public void listen() {
        System.out.println("Waiting for connections ...");
        while (true) {
            Socket socket = null;
            try {
                socket = server.accept();
                String message = receiveMessage(socket);
                int id = Integer.parseInt(message.split("::")[0]);
                int port = Integer.parseInt(message.split("::")[1]);
                OnlineUser chatter = super.user.getUserBookManager().chooseOnlineUser(id);
                new ThreadChatServer(chatter, socket, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String receiveMessage(Socket client) {
        DataInputStream reveive;
        String message = "";
        try {
            reveive = new DataInputStream(client.getInputStream());
            message = reveive.readUTF();
            System.out.println("Receive " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    
    public void close() {
        try {
			server.close();
		} catch (Exception e) {
		}
    }
}
