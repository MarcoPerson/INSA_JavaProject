package MarcoWalter.ChatProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.UserBookManager;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddUsersController {
	private Stage stage;
	private String message;
	
	@FXML
	Label groupNameLabel;
	@FXML
	private VBox listGroupUserVbox;
	
	private HashMap<String, OnlineUser> pseudoMap = new HashMap<String, OnlineUser>();

	@FXML
	private void initialize() {
		
	}
	
	public void initializeUsers(UserBookManager book) {
		for(OnlineUser user: book.getListOfOnlineUser()) {
			pseudoMap.put(user.getPseudo(), user);
			addUserToGroup(user.getPseudo());
		}
	}
	
	public void setGroupNameText(String text) {
		groupNameLabel.setText(text);
	}

	@FXML
	public void addUserToGroup(String pseudo) {
		HBox hBox = new HBox();
		CheckBox checkBox = new CheckBox();
		checkBox.setText(pseudo);
		hBox.getChildren().add(checkBox);
		listGroupUserVbox.getChildren().add(hBox);
	}

	@FXML
	public void addUsersGroup() {
		ArrayList<OnlineUser> listOfSelectedMembers = new ArrayList<OnlineUser>();
		List<String> selectedUsers = new ArrayList<String>();
		for (int i = 0; i < listGroupUserVbox.getChildren().size() - 1; i++) {
			HBox hbox = (HBox) listGroupUserVbox.getChildren().get(i+1);
			CheckBox checkBox = (CheckBox) hbox.getChildren().get(0);
			if (checkBox.isSelected()) {
				selectedUsers.add(checkBox.getText());
				listOfSelectedMembers.add(pseudoMap.get(checkBox.getText()));
			}
		}
		for(OnlineUser selectedUser: listOfSelectedMembers) {
			try {
				App.meSocketUDP.sendMessage(selectedUser, App.me.getId(), App.me.getPseudo(), message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		stage.close();
		HomeController.getInstance().showNotification("Selected Users Were Added");
	}

	@FXML
	
	private void cancelGroup() throws IOException {
		stage.close();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
