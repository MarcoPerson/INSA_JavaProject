package MarcoWalter.ChatProject.TcpControllers;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UserSocketServer {
    ServerSocket server;

    public UserSocketServer() {
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
                new ThreadChatServer(id, socket, port);
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
}
