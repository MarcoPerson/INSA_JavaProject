package MarcoWalter.ChatProject;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	private TextField pseudoField;

    @FXML
    private void login() throws IOException {
        if(pseudoField.getText().toString().equals("Marco")) {
        	App.setRoot("home");        
        	App.getStage().setTitle("Home");
        }
    }
}
