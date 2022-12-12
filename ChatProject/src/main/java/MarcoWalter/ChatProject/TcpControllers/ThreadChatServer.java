package MarcoWalter.ChatProject.TcpControllers;

import java.net.Socket;

import MarcoWalter.ChatProject.Models.OnlineUser;

public class ThreadChatServer extends Thread {
    private Socket entrie;
    private OnlineUser user;
    private int port;

    public ThreadChatServer(int chatId, Socket _chatSocket, int _port) {
        entrie = _chatSocket;
        port = _port;
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
            OnlineUser marco = new OnlineUser("marco", 235, null, 1235);
            System.out.println("Starting chat With Id ..." + marco.getId());
            new TreadMessageSender(chatSocket);
            new ThreadMessageReceiver(marco, chatSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
