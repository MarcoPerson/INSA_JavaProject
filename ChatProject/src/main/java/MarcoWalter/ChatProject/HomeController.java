package MarcoWalter.ChatProject;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class HomeController {
	@FXML
	private TableView onlineUsersList;
	
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
    	ObservableList<String> items = onlineUsersList.getItems();
    	for(String c : "Je Suis Walter en Séparé".split(" ")) {
    		items.add(c);
    	}
    }
    
}