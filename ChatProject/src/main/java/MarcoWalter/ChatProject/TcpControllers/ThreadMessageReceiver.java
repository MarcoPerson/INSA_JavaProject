package MarcoWalter.ChatProject.TcpControllers;

import java.io.DataInputStream;
import java.net.Socket;

import MarcoWalter.ChatProject.Models.OnlineUser;

public class ThreadMessageReceiver extends Thread {
    private OnlineUser user;
    private Socket socket;
    DataInputStream receive;

    public ThreadMessageReceiver(OnlineUser _user, Socket _socket) {
        user = _user;
        socket = _socket;
        try {
            receive = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        start();
    }

    public void run() {
        boolean bool = true;
        while (bool) {
            bool = receiveMessage();
        }
    }

    public boolean receiveMessage() {
        try {
            String message;
            message = receive.readUTF();
            if (message.equals("::end")) {
                socket.close();
                return false;
            } else {
                System.out.println(user.getPseudo() + " : " + message);
            }
        } catch (Exception e) {
            if(e.toString().equals("java.net.SocketException: Socket closed")){
                return false;
            }
        }
        return true;
    }
}
