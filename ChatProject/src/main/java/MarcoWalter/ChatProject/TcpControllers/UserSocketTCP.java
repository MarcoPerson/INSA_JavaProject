package MarcoWalter.ChatProject.TcpControllers;

import MarcoWalter.ChatProject.Models.OnlineUser;

public class UserSocketTCP {
    protected OnlineUser user;

    public UserSocketTCP(OnlineUser _user){
        user = _user;
    }

    public boolean startChat(OnlineUser user){
        return false;
    }
    
}