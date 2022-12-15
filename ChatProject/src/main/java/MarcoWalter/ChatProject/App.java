package MarcoWalter.ChatProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

import MarcoWalter.ChatProject.Models.User;
import MarcoWalter.ChatProject.TcpControllers.UserSocketClient;
import MarcoWalter.ChatProject.TcpControllers.UserSocketServer;
import MarcoWalter.ChatProject.UdpControllers.UserSocketUDP;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // Connecting to the System
        User me = new User(1);
        int id;
        String password;
        Scanner myChooser = new Scanner(System.in);

        System.out.print("Enter your id : ");
        id = Integer.parseInt(myChooser.nextLine());

        System.out.print("Enter your password : ");
        password = myChooser.nextLine();
        
    	try {
            UserSocketUDP meSocketUDP = new UserSocketUDP(me);
            boolean agreed = false;
            do {
                System.out.print("Choose a new Pseudo : ");
                me.modifyPseudo(myChooser.nextLine());
                me.connectToNetwork(meSocketUDP);
                agreed = meSocketUDP.waitForAggrement();
            } while (agreed == false);

            meSocketUDP.broadcast(me.getId(), me.getPseudo(), "newUser");
            Thread reception = new Thread(() -> meSocketUDP.receiveMessage());
            reception.start();

    	}
    	catch(Exception e) {
            e.printStackTrace();
    	}
        myChooser.close();

        // Listening on TCP for incoming chat demand
        UserSocketServer mySocketServer = new UserSocketServer(me);
        Thread listener = new Thread(() -> mySocketServer.listen());
        listener.start();

        // Asking for a chat with the first user
        UserSocketClient mysocket = new UserSocketClient(me);
        int oneId = me.getUserBookManager().getUserBook().keySet().stream().mapToInt(Integer::intValue).toArray()[0];
        mysocket.initChat(me.getUserBookManager().chooseOnlineUser(oneId));
        launch();
    }

}