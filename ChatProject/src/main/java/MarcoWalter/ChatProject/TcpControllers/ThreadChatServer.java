package MarcoWalter.ChatProject.TcpControllers;

import java.net.Socket;

import MarcoWalter.ChatProject.App;
import MarcoWalter.ChatProject.ControllerManager;
import MarcoWalter.ChatProject.HomeController;
import MarcoWalter.ChatProject.MessageController;
import MarcoWalter.ChatProject.Models.OnlineUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ThreadChatServer extends Thread {
    private Socket entrie;
    private OnlineUser user;
    private int port;
   
    public ThreadChatServer(OnlineUser chatter, Socket _chatSocket, int _port) {
        entrie = _chatSocket;
        port = _port;
        user = chatter;
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
                        
            UserSocketTCP.socketMap.put(user.getId(), chatSocket);
            FXMLLoader messageLoader = new FXMLLoader(App.class.getResource("message.fxml"));
            AnchorPane load = messageLoader.load();
            MessageController controller = messageLoader.getController();
            App.discussionScenes.put(user.getId(), load);
            App.discussionControllers.put(user.getId(), controller);
            controller.setSocket(chatSocket);
            controller.setUser(user);
            controller.setUserPseudoText(user.getPseudo());
            
            new ControllerManager().setDiscussionScene(HomeController.getInstance(), App.discussionScenes.get(user.getId()));
            
            System.out.println("Starting chat With id : " + user.getId());
//            new TreadMessageSender(user, chatSocket);
            UserSocketTCP.threadMap.put(user.getId(), new ThreadMessageReceiver(user, chatSocket));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
