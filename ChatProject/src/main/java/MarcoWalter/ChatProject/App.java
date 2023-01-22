package MarcoWalter.ChatProject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Scanner;

import MarcoWalter.ChatProject.DatabaseControllers.DbControllers;
import MarcoWalter.ChatProject.Models.DataBase;
import MarcoWalter.ChatProject.Models.GroupData;
import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.User;
import MarcoWalter.ChatProject.TcpControllers.UserSocketClient;
import MarcoWalter.ChatProject.TcpControllers.UserSocketServer;
import MarcoWalter.ChatProject.TcpControllers.UserSocketTCP;
import MarcoWalter.ChatProject.UdpControllers.UserSocketUDP;

/**
 * JavaFX App
 */
public class App {

	private static Scene scene;
	private static Stage stage;
	private static LoginController loginController;
	private static HomeController homeController;
	private static Object controller;

	public static HashMap<Integer, AnchorPane> discussionScenes = new HashMap<>();
	public static HashMap<Integer, MessageController> discussionControllers = new HashMap<>();
	public static HashMap<InetAddress, AnchorPane> discussionGroupScenes = new HashMap<>();
	public static HashMap<InetAddress, MessageController> discussionGroupControllers = new HashMap<>();
	public static UserSocketClient mysocket;
	public static UserSocketUDP meSocketUDP;
	public static UserSocketServer mySocketServer;
	public static Thread reception;
	public static Thread listener;
	public static User me;
	public static DbControllers dbController;

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

			if (agreed) {
				meSocketUDP.broadcast(me.getId(), me.getPseudo(), "newUser");
				reception = new Thread(() -> meSocketUDP.receiveMessage());
				reception.start();
				new ControllerManager().setconnectionMessageText(new LoginController().getInstance(),
						"Connected sucessfully");
				mysocket = new UserSocketClient(me);

				ConnectToDataBase(id, pseudo, password);
				scene.setOnKeyPressed(null);
				setRoot("home");

				File f = new File(".info");
				if (f.exists() && !f.isDirectory()) {
				} else {
					HomeController.getInstance()
							.showNotification("To Send a Message, You Can Press On 'SHIFT + ENTER'");
					FileWriter fileWriter = new FileWriter(".info");
					PrintWriter printWriter = new PrintWriter(fileWriter);
					printWriter.print("");
					printWriter.close();
				}

				// Listening on TCP for incoming chat demand
				mySocketServer = new UserSocketServer(me);
				listener = new Thread(() -> mySocketServer.listen());
				listener.start();
			} else {
				meSocketUDP.close();
				new ControllerManager().setconnectionMessageText(new LoginController().getInstance(),
						"Pseudo already used !!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void ConnectToDataBase(int id, String pseudo, String password) {
		DataBase dataBase = new DataBase(String.valueOf(id), password, "Chat.db");
		dataBase.createNewDataBase(String.valueOf(id), password);

		dbController = new DbControllers(dataBase, String.valueOf(id), password);
		dbController.createUserMessages();
	}

	public static void StartDiscussion(OnlineUser chatUser) {
		System.out.println(chatUser.getPseudo());
		System.out.println(chatUser.getId());
		System.out.println(chatUser.getIpAddress());
		System.out.println(chatUser.getPort());

		// Asking for a chat with the first user
		if (UserSocketTCP.socketMap.containsKey(chatUser.getId())) {
			new ControllerManager().setDiscussionScene(HomeController.getInstance(),
					discussionScenes.get(chatUser.getId()));
		} else {
			mysocket.initChat(chatUser);
		}
	}

	public static void enterGroup(GroupData selectedGroup) {
		new ControllerManager().setDiscussionScene(HomeController.getInstance(),
				discussionGroupScenes.get(selectedGroup.getIpAddress()));
	}

	public static void main(String[] args) {
		Platform.startup(() -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					stage = new Stage();
					stage.getIcons().add(new Image("file:src/main/resources/Images/chat_icon.png"));
					try {
						scene = new Scene(loadFXML("login"), 900, 500);
						new ControllerManager().setSendButtonAction(LoginController.getInstance());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					stage.setScene(scene);
					stage.setOnCloseRequest(event -> {
						try {
							if (App.listener != null)
								new HomeController().getInstance().deconnectToLogin();
							System.exit(0);
						} catch (IOException e) {
							e.printStackTrace();
						}
					});

					stage.show();
					File directory = new File("files");
					if (!directory.exists()) {
						directory.mkdirs();
					}
				}
			});
		});
	}

}