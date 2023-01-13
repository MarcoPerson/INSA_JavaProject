package MarcoWalter.ChatProject.TcpControllers;

import MarcoWalter.ChatProject.Models.User;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class UserSocketTCP {
    protected User user;
    public static Map<Integer, Socket> socketMap = new HashMap<>();

    public UserSocketTCP(User _user){
        user = _user;
    }
}