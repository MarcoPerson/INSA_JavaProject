package MarcoWalter.ChatProject.TcpControllers;

import java.net.ServerSocket;
import java.net.Socket;

import MarcoWalter.ChatProject.App;
import MarcoWalter.ChatProject.ControllerManager;
import MarcoWalter.ChatProject.HomeController;
import MarcoWalter.ChatProject.MessageController;
import MarcoWalter.ChatProject.Models.OnlineUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ThreadChatClient extends Thread {
    private ServerSocket chatSocket;
    private OnlineUser user;

    public ThreadChatClient(ServerSocket _chatSocket, OnlineUser _user) {
        chatSocket = _chatSocket;
        user = _user;
        start();
    }

    public void run() {
        chat();
    }

    public void chat() {
        try {
            Socket entrie = chatSocket.accept();
            
            UserSocketTCP.socketMap.put(user.getId(), entrie);
            FXMLLoader messageLoader = new FXMLLoader(App.class.getResource("message.fxml"));
            AnchorPane load = messageLoader.load();
            MessageController controller = messageLoader.getController();
            App.discussionScenes.put(user.getId(), load);
            App.discussionControllers.put(user.getId(), controller);
            controller.setSocket(entrie);
            controller.setUser(user);
            controller.setuserPseudoText(user.getPseudo());
            
            new ControllerManager().setDiscussionScene(HomeController.getInstance(), App.discussionScenes.get(user.getId()));
            
//            new TreadMessageSender(user, entrie);
            new ThreadMessageReceiver(user, entrie);
            chatSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
