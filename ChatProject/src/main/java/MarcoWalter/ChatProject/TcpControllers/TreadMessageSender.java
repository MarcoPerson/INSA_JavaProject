package MarcoWalter.ChatProject.TcpControllers;

import java.io.DataOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import MarcoWalter.ChatProject.DatabaseControllers.DbControllers;
import MarcoWalter.ChatProject.Models.DataBase;
import MarcoWalter.ChatProject.Models.OnlineUser;

public class TreadMessageSender extends Thread {
    private Socket socket;
    DataOutputStream send;
//    Scanner input = new Scanner(System.in);
    DataBase dataBase;
    DbControllers dbConn;
    OnlineUser user;
    String message;
    
    public TreadMessageSender(OnlineUser _user, Socket _socket, String _message) {
        socket = _socket;
        user = _user;
        message = _message;
        try {
            send = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataBase = new DataBase("", "", "Chat.db");
        dbConn = new DbControllers(dataBase, "", "");
        start();
    }

    public void run() {
        try {
//            boolean bool = true;
//            String message;
//            while (bool) {
//                message = getMessage();
                sendMessage(message);
                if (message.equals("::end")) {
                    socket.close();
//                    input.close();
//                    bool = false;
                }
                else{
                    String pattern = "MM/dd/yyyy HH:mm:ss";
                    DateFormat df = new SimpleDateFormat(pattern);
                    Date today = Calendar.getInstance().getTime();
                    String todayAsString = df.format(today);
                    dbConn.insertLine(user.getId(), 0, message, todayAsString);
                }
//            }
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

//    public String getMessage() {
//        System.out.print(">> ");
//        String message = input.nextLine();
//        return message;
//
//    }
}
