package MarcoWalter.ChatProject.TcpControllers;

import java.net.Socket;

import MarcoWalter.ChatProject.DatabaseControllers.DbControllers;
import MarcoWalter.ChatProject.Models.DataBase;
import MarcoWalter.ChatProject.Models.OnlineUser;

public class ThreadChatServer extends Thread {
    private Socket entrie;
    private OnlineUser user;
    private int port;
   
    public ThreadChatServer(OnlineUser chatter, Socket _chatSocket, int _port) {
        entrie = _chatSocket;
        port = _port;
        user = chatter;
        start();
    }

    public void run() {
        try {
            chat();
        } catch (Exception e) {
            System.out.println(getName() + " : " + e.getMessage());
        }
    }

    public void chat() {
        try {
            Socket chatSocket = new Socket(entrie.getInetAddress(), port);
            System.out.println("Starting chat With id : " + user.getId());
            new TreadMessageSender(user, chatSocket);
            new ThreadMessageReceiver(user, chatSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
