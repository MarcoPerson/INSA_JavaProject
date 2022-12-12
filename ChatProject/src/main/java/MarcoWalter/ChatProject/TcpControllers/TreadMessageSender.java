package MarcoWalter.ChatProject.TcpControllers;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TreadMessageSender extends Thread {
    private Socket socket;
    DataOutputStream send;
    Scanner input = new Scanner(System.in);

    public TreadMessageSender(Socket _socket) {
        socket = _socket;
        try {
            send = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        start();
    }

    public void run() {
        try {
            boolean bool = true;
            String message;
            while (bool) {
                message = getMessage();
                sendMessage(message);
                if (message.equals("::end")) {
                    socket.close();
                    input.close();
                    bool = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message) {
        try {
            send.writeUTF(message);
            send.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getMessage() {
        System.out.print(">> ");
        String message = input.nextLine();
        return message;

    }
}
