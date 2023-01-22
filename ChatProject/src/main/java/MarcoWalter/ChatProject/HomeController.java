package MarcoWalter.ChatProject;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import MarcoWalter.ChatProject.Models.GroupData;
import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.TcpControllers.UserSocketTCP;
import MarcoWalter.ChatProject.UdpControllers.UserSocketUDP;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HomeController{
	private static HomeController instance;

	@FXML
	private TableView<OnlineUser> onlineUsersList;

	@FXML
	private TableColumn<OnlineUser, String> onlineUsersTable;
	
	@FXML
	private TableView<GroupData> groupList;

	@FXML
	private TableColumn<GroupData, String> groupTable;

	@FXML
	BorderPane discussionScene;

	public static ObservableList<OnlineUser> items;
	
	public static ObservableList<GroupData> groupItems;

	public static HomeController getInstance() {
		if (instance == null) {
			instance = new HomeController();
		}
		return instance;
	}

	@FXML
	private void initialize() {
		instance = this;
		App.getStage().setTitle("Home - " + App.me.getPseudo());
		
		items = FXCollections.observableArrayList();
		onlineUsersList.setItems(items);
		onlineUsersTable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPseudo()));
		for (OnlineUser user : App.me.getUserBookManager().getUserBook().values()) {
			System.out.println(user.getPseudo());
			items.add(user);
		}
		onlineUsersList.setOnMouseClicked(event -> {
			OnlineUser selectedUser = onlineUsersList.getSelectionModel().getSelectedItem();
			if (selectedUser != null) {
				App.StartDiscussion(selectedUser);
			}
		});
		
		groupItems = FXCollections.observableArrayList();
		groupList.setItems(groupItems);
		groupTable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGroupName()));
		for (GroupData data : App.me.getUserBookManager().getGroupBook().values()) {
			System.out.println(data.getGroupName());
			groupItems.add(data);
		}
		groupList.setOnMouseClicked(event -> {
			GroupData selectedGroup = groupList.getSelectionModel().getSelectedItem();
			if (selectedGroup != null) {
				App.enterGroup(selectedGroup);
			}
		});
	}

	@FXML
	public void setDiscussion(AnchorPane discussion) {
		discussionScene.setCenter(discussion);
	}

	@FXML
	public void deconnectToLogin() throws IOException {
		App.me.disconnectectFromNetwork(App.meSocketUDP);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		for (Thread onlineUserThread : UserSocketTCP.threadMap.values()) {
			onlineUserThread.stop();
		}
		UserSocketTCP.threadMap.clear();
		
		App.listener.stop();
		App.listener = null;
		App.reception.stop();
		App.mySocketServer.close();
		App.meSocketUDP.close();
		App.discussionControllers.clear();
		App.discussionScenes.clear();
		App.discussionGroupControllers.clear();
		App.discussionGroupScenes.clear();
		items.clear();
		groupItems.clear();
		
		for (Socket onlineUserSocket : UserSocketTCP.socketMap.values()) {
			onlineUserSocket.close();
		}
		
		for (Thread groupThread : UserSocketUDP.threadMap.values()) {
			groupThread.stop();
		}
		
		UserSocketUDP.threadMap.clear();
		UserSocketTCP.socketMap.clear();
		App.me.getUserBookManager().deleteAllOnlineUser();
		App.me.getUserBookManager().deleteAllGroupData();
		App.setRoot("login");
		App.getStage().setTitle("Login");
	}

	@FXML
	private void changePseudo() throws IOException {
		Stage modal = new Stage();
		modal.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("pseudo.fxml"));
		AnchorPane pseudoPane = fxmlLoader.load();
		PseudoController controller = fxmlLoader.getController();
		modal.getIcons().add(new Image("file:src/main/resources/Images/chat_icon.png"));
		Scene scene = new Scene(pseudoPane, 300, 200);
		scene.setOnKeyPressed(event -> {
		    if (event.getCode() == KeyCode.ENTER) {
		    	try {
					controller.changePseudo();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		});
		modal.setScene(scene);
		modal.setTitle("Change Pseudo");
		modal.setResizable(false);
		controller.setStage(modal);
		modal.show();
	}

	@FXML
	private void createChatGroup() throws IOException {
		Stage modal = new Stage();
		modal.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("group.fxml"));
		AnchorPane pseudoPane = fxmlLoader.load();
		GroupController controller = fxmlLoader.getController();
		modal.getIcons().add(new Image("file:src/main/resources/Images/chat_icon.png"));
		Scene scene = new Scene(pseudoPane, 600, 400);
		scene.setOnKeyPressed(event -> {
		    if (event.getCode() == KeyCode.ENTER) {
		    	try {
					controller.createGroup();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		});
		modal.setScene(scene);
		modal.setTitle("Create Group Chat");
		modal.setResizable(false);
		controller.setStage(modal);
		controller.initializeUsers(App.me.getUserBookManager());
		modal.show();
	}

	public void updateTableList() {
		items.clear();
		for (OnlineUser user : App.me.getUserBookManager().getUserBook().values()) {
			items.add(user);
		}
	}
	
	public AnchorPane getMessagePane(String disconnectText) {
        AnchorPane root = new AnchorPane();
        root.setMinHeight(0);
        root.setMinWidth(0);
        root.setPrefHeight(450);
        root.setPrefWidth(600);

        Label userPseudo = new Label();
        userPseudo.setAlignment(Pos.CENTER);
        userPseudo.setLayoutX(100);
        userPseudo.setLayoutY(5);
        userPseudo.setPrefHeight(35);
        AnchorPane.setLeftAnchor(userPseudo, 0.0);
        AnchorPane.setRightAnchor(userPseudo, 0.0);
        AnchorPane.setTopAnchor(userPseudo, 5.0);

        ScrollPane scrollMessage = new ScrollPane();
        scrollMessage.setLayoutX(165);
        scrollMessage.setLayoutY(66);
        AnchorPane.setBottomAnchor(scrollMessage, 80.0);
        AnchorPane.setLeftAnchor(scrollMessage, 0.0);
        AnchorPane.setRightAnchor(scrollMessage, 0.0);
        AnchorPane.setTopAnchor(scrollMessage, 40.0);

        VBox messageBox = new VBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPrefHeight(250);
        messageBox.setPrefWidth(580);
        messageBox.setPadding(new Insets(10, 10, 10, 10));

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefHeight(100);
        hbox.setPrefWidth(200);

        Label userDisconnect = new Label();
        userDisconnect.setText(disconnectText);
        userDisconnect.setFont(new Font(21));
        hbox.getChildren().add(userDisconnect);

        messageBox.getChildren().add(hbox);
        scrollMessage.setContent(messageBox);
        root.getChildren().addAll(userPseudo, scrollMessage);
        return root;
    }
	
	public void showNotification(String message) {
		Stage modal = new Stage();
		modal.initModality(Modality.APPLICATION_MODAL);

		modal.getIcons().add(new Image("file:src/main/resources/Images/chat_icon.png"));
		
	    AnchorPane root = new AnchorPane();
	    VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutX(-9);
        vBox.setPrefHeight(125);
        vBox.setPrefWidth(250);
        vBox.setSpacing(30);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);

        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        
        textFlow.setPrefHeight(11);
        textFlow.setPrefWidth(301);
        textFlow.setPadding(new Insets(10, 10, 10, 10));

        Text notificationText = new Text(message);
        notificationText.setTextAlignment(TextAlignment.CENTER);
        notificationText.setLineSpacing(10);
        notificationText.setStrokeWidth(0);

        textFlow.getChildren().add(notificationText);

        Button okButton = new Button("OK");
        okButton.setMnemonicParsing(false);
        okButton.setOnAction(e -> modal.close());
        
        vBox.getChildren().addAll(textFlow, okButton);
        root.getChildren().add(vBox);
        
        Scene scene = new Scene(root, 250, 175);
        scene.setOnKeyPressed(event -> {
		    if (event.getCode() == KeyCode.ENTER) {
		    	try {
					okButton.fire();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		});
		modal.setScene(scene);
		modal.setTitle("Notification");
		modal.setResizable(false);
		modal.show();

        

	}

	public void manageDisconnectedUser(OnlineUser newuser) {
		App.me.getUserBookManager().removeOnlineUser(newuser.getId());
		updateTableList();
		try {
			if(UserSocketTCP.socketMap.get(newuser.getId()) != null) {
				UserSocketTCP.threadMap.get(newuser.getId()).stop();
				UserSocketTCP.threadMap.remove(newuser.getId());
				UserSocketTCP.socketMap.get(newuser.getId()).close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		UserSocketTCP.socketMap.remove(newuser.getId());
		App.discussionControllers.remove(newuser.getId());
		App.discussionScenes.remove(newuser.getId());
		
		discussionScene.setCenter(getMessagePane(newuser.getPseudo() + " went Offline"));
	}
}