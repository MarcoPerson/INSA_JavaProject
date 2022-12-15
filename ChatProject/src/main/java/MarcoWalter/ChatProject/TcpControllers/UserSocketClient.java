package MarcoWalter.ChatProject.TcpControllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.User;

public class UserSocketClient extends UserSocketTCP{
    private Socket socketForConnect;
    private int chatId = 0;

    public UserSocketClient(User _user) {
    	super(_user);
    }

    public void initChat(OnlineUser user){
        try {
            socketForConnect = new Socket(user.getIpAddress(), 3101);
            String messageTosend;
            int port = findFreePort();
            messageTosend = String.valueOf(super.user.getId()).concat("::").concat(String.valueOf(port));
            sendMessage(messageTosend, socketForConnect);
    
            socketForConnect.close();
    
            ServerSocket chatSocket = new ServerSocket(port);
            new ThreadChatClient(chatSocket, user);
            chatId += 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message, Socket userToConnectSocket) {
        DataOutputStream send;
        try {
            send = new DataOutputStream(userToConnectSocket.getOutputStream());
            send.writeUTF(message);
            send.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void receiveMessage(Socket socket){
        DataInputStream reveive;
        String message;
        try {
            reveive = new DataInputStream(socket.getInputStream());
            message = reveive.readUTF();
            System.out.println("Receive " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int findFreePort() {
        int port = 0;
        // For ServerSocket port number 0 means that the port number is automatically allocated.
        try (ServerSocket socket = new ServerSocket(0)) {
            // Disable timeout and reuse address after closing the socket.
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
        } catch (IOException ignored) {
        }
        if (port > 0) {
            return port;
        }
        throw new RuntimeException("Could not find a free port");
    }

}