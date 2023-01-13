package MarcoWalter.ChatProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	private static LoginController instance;
	
	@FXML 
	Label connectionMessage;
	
	@FXML
	private TextField idField;
	
	@FXML
	private TextField pseudoField;
	
	@FXML
	private PasswordField passwordField;
	
	String id;
	String pseudo;
	String password;
	
	String fileName = ".connection_info.bin";
	
	public void setconnectionMessageText(String text) {
		System.out.println(text);
		connectionMessage.setText(text);
	}
	
	public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }
	
	@FXML
	private void initialize() {
		instance = this;
		App.getStage().setTitle("Login");
	}
	
    @FXML
    private void login() throws IOException {
    	if(!idField.getText().toString().isEmpty() && !pseudoField.getText().toString().isEmpty() && !passwordField.getText().toString().isEmpty()) {
			File f = new File(fileName);
			if(f.exists() && !f.isDirectory()) { 
				BufferedReader reader;
				reader = new BufferedReader(new FileReader(fileName));
				id = reader.readLine();
				password = reader.readLine();
			    reader.close();
				if(idField.getText().toString().equals(id) && passwordField.getText().toString().equals(password)) {
					pseudo = pseudoField.getText().toString();
					App.ConnectToTheSystem(Integer.parseInt(id), pseudo, password);
				}else {
					connectionMessage.setText("Error with id or password !!");
				}
			}else {
		    	FileWriter fileWriter = new FileWriter(fileName);
		        PrintWriter printWriter = new PrintWriter(fileWriter);
		        printWriter.print(idField.getText().toString());
		        printWriter.print("\n");
		        printWriter.print(passwordField.getText().toString());
		        printWriter.close();
		        id = idField.getText().toString();
				pseudo = pseudoField.getText().toString();
				password = passwordField.getText().toString();
		        App.ConnectToTheSystem(Integer.parseInt(id), pseudo, password);
			}
		}else {
			connectionMessage.setText("Please fill or the entries !!");
		}
    }
}
