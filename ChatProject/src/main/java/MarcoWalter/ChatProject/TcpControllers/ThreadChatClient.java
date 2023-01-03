package MarcoWalter.ChatProject.TcpControllers;

import java.net.ServerSocket;
import java.net.Socket;

import MarcoWalter.ChatProject.Models.OnlineUser;

public class ThreadChatClient extends Thread {
    private ServerSocket chatSocket;
    private OnlineUser user;

    public ThreadChatClient(ServerSocket _chatSocket, OnlineUser _user) {
        chatSocket = _chatSocket;
        user = _user;
        start();
    }

    public void run() {
        chat();
    }

    public void chat() {
        try {
            Socket entrie = chatSocket.accept();
            new TreadMessageSender(user, entrie);
            new ThreadMessageReceiver(user, entrie);
            chatSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
