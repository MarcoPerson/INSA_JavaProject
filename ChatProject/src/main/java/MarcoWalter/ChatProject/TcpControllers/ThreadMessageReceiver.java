package MarcoWalter.ChatProject.TcpControllers;

import java.io.DataInputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import MarcoWalter.ChatProject.DatabaseControllers.DbControllers;
import MarcoWalter.ChatProject.Models.DataBase;
import MarcoWalter.ChatProject.Models.OnlineUser;

public class ThreadMessageReceiver extends Thread {
    private OnlineUser user;
    private Socket socket;
    DataInputStream receive;
    DataBase dataBase;
    DbControllers dbConn;

    public ThreadMessageReceiver(OnlineUser _user, Socket _socket) {
        user = _user;
        socket = _socket;
        try {
            receive = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataBase = new DataBase("", "", "Chat.db");
        dbConn = new DbControllers(dataBase, "", "");
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
                String pattern = "MM/dd/yyyy HH:mm:ss";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String todayAsString = df.format(today);
                dbConn.insertLine(user.getId(), 1, message, todayAsString);
            }
        } catch (Exception e) {
            if(e.toString().equals("java.net.SocketException: Socket closed")){
                return false;
            }
        }
        return true;
    }
}
