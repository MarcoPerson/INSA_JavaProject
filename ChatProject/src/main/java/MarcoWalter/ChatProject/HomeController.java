package MarcoWalter.ChatProject;

import java.io.IOException;
import java.util.Random;

import MarcoWalter.ChatProject.Models.OnlineUser;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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
        for(OnlineUser user : App.me.getUserBookManager().getUserBook().values()) {
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
    public void setDiscussion(AnchorPane discussion){
        discussionScene.setCenter(discussion);
    }
	
    @FXML
    private void deconnectToLogin() throws IOException {
        App.setRoot("login");
        App.getStage().setTitle("Login");
    }
    
    @FXML
    private void sendMessage() throws IOException {
        App.setRoot("login");
        App.getStage().setTitle("Login");
    }
    
    @FXML
    private void changePseudo() throws IOException {
    	int rd = new Random().nextInt(100);
    	items.add(new OnlineUser("Marco - "+String.valueOf(rd), rd, null, rd));
    	App.me.getUserBookManager().addOnlineUser(rd, new OnlineUser("MA"+String.valueOf(rd), rd, null, rd));
    }
    
}