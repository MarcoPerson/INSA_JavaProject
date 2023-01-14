package MarcoWalter.ChatProject;

import java.io.IOException;
import java.util.Random;

import MarcoWalter.ChatProject.Models.OnlineUser;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController {
	private static HomeController instance;

	@FXML
	private TableView<OnlineUser> onlineUsersList;

	@FXML
	private TableColumn<OnlineUser, String> onlineUsersTable;

	@FXML
	BorderPane discussionScene;

	public static ObservableList<OnlineUser> items;

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
	}

	@FXML
	public void setDiscussion(AnchorPane discussion) {
		discussionScene.setCenter(discussion);
	}

	@FXML
	private void deconnectToLogin() throws IOException {
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
		modal.setScene(scene);
		modal.setTitle("Change Pseudo");
		modal.setResizable(false);
		controller.setStage(modal);
		modal.show();
	}

	@FXML
	private void createChatGroup() throws IOException {
		int rd = new Random().nextInt(100);
		int key = App.me.getUserBookManager().getUserBook().keySet().stream().mapToInt(Integer::intValue).toArray()[0];
		OnlineUser myUser = App.me.getUserBookManager().getUserBook().get(key);
		myUser.setPseudo("Rare - " + rd);
		System.out.println(App.me.getUserBookManager().getUserBook().get(key).getPseudo());
		items.clear();
		for (OnlineUser user : App.me.getUserBookManager().getUserBook().values()) {
			System.out.println(user.getPseudo());
			items.add(user);
		}
	}

	public void updateTableList() {
		items.clear();
		for (OnlineUser user : App.me.getUserBookManager().getUserBook().values()) {
			items.add(user);
		}
	}
}