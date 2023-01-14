package MarcoWalter.ChatProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Scanner;

import MarcoWalter.ChatProject.DatabaseControllers.DbControllers;
import MarcoWalter.ChatProject.Models.DataBase;
import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.User;
import MarcoWalter.ChatProject.TcpControllers.UserSocketClient;
import MarcoWalter.ChatProject.TcpControllers.UserSocketServer;
import MarcoWalter.ChatProject.TcpControllers.UserSocketTCP;
import MarcoWalter.ChatProject.UdpControllers.UserSocketUDP;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private static LoginController loginController;
    private static HomeController homeController;
    private static Object controller;
    
    
    public static HashMap<Integer, AnchorPane> discussionScenes = new HashMap<>();
    public static HashMap<Integer, MessageController> discussionControllers = new HashMap<>();
    public static UserSocketClient mysocket;
    public static UserSocketUDP meSocketUDP;
    public static Thread reception;
    public static User me;

    @Override
    public void start(Stage stage) throws IOException {
    	this.stage = stage;
    	stage.getIcons().add(new Image("file:src/main/resources/Images/chat_icon.png"));
        scene = new Scene(loadFXML("login"), 900, 500);
        stage.setScene(scene);
        stage.show();
    }
    
    public static Stage getStage() {
    	return stage;
    }
    
    public static Scene getScene() {
    	return scene;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        controller = fxmlLoader.getController();
        return fxmlLoader.load();
    }
    
    public static void ConnectToTheSystem(int id, String pseudo, String password) {
    	me = new User(id);
     	try {
             meSocketUDP = new UserSocketUDP(me);
             boolean agreed = false;
             me.modifyPseudo(pseudo);
             me.connectToNetwork(meSocketUDP);
             agreed = meSocketUDP.waitForAggrement();
             
             if(agreed) {
            	 meSocketUDP.broadcast(me.getId(), me.getPseudo(), "newUser");
                 reception = new Thread(() -> meSocketUDP.receiveMessage());
                 reception.start();
                 new ControllerManager().setconnectionMessageText(new LoginController().getInstance(), "Connected sucessfully");
                 mysocket = new UserSocketClient(me);
                 //ConnectToDataBase(id, pseudo, password);
                 setRoot("home");
             }else {
            	 new ControllerManager().setconnectionMessageText(new LoginController().getInstance(), "Pseudo already used !!!");
             }
     	}
     	catch(Exception e) {
             e.printStackTrace();
     	}

         // Listening on TCP for incoming chat demand
         UserSocketServer mySocketServer = new UserSocketServer(me);
         Thread listener = new Thread(() -> mySocketServer.listen());
         listener.start();
    }
    
    public static void ConnectToDataBase(int id, String pseudo, String password) {
    	DataBase dataBase = new DataBase(String.valueOf(id), password, "Chat.db");
        dataBase.createNewDataBase(String.valueOf(id), password);
        
        DbControllers dbConn = new DbControllers(dataBase, String.valueOf(id), password);
        dbConn.createNewTable();
//        dbConn.insertLine(237, 1, "hello", "31/01/2023 11:53:01");
//        dbConn.insertLine(228, 1, "hi", "31/01/2023 11:53:01");
//        dbConn.insertLine(237, 1, "good", "31/01/2023 11:53:01");
        for(String message : dbConn.getMessageWith(237)) {
        	System.out.println(message);
        }
    }
    
    public static void StartDiscussion(OnlineUser chatUser) {
    	System.out.println(chatUser.getPseudo());
    	System.out.println(chatUser.getId());
    	System.out.println(chatUser.getIpAddress());
    	System.out.println(chatUser.getPort());
    	
    	// Asking for a chat with the first user
	    if(UserSocketTCP.socketMap.containsKey(chatUser.getId())) {
	    	new ControllerManager().setDiscussionScene(HomeController.getInstance(), discussionScenes.get(chatUser.getId()));
	    }else {
	    	mysocket.initChat(chatUser);
	    }
    }

    public static void main(String[] args) {
        launch();  
    }

}