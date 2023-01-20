package MarcoWalter.ChatProject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.User;
import MarcoWalter.ChatProject.TcpControllers.TreadMessageSender;
import MarcoWalter.ChatProject.UdpControllers.MulticastSender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageController extends ModelController  {
	private Socket socket;
	private OnlineUser user;
	public HashMap<Integer, OnlineUser> GroupUsers = new HashMap<Integer, OnlineUser>();
	private InetAddress groupeIP; 
	private int multicastPort;
	private String groupName;
	private User mySelf;
	
	@FXML 
	Label userPseudo;
	
	@FXML 
	private Button sendMessageButton;
	
	@FXML 
	private Button sendFileButton;
	
	@FXML 
	private Button addUsersToGroupButton;
	
	@FXML
	private ScrollPane scrollMessage;
	
	@FXML
	private VBox messageBox;
	
	@FXML
	private TextArea messageToSendField;
	
	public void setUserPseudoText(String text) {
		userPseudo.setText(text);
		if(groupName == null) {
			addUsersToGroupButton.setVisible(false);
		}else {
			sendFileButton.setVisible(false);
		}
	}
	
	@FXML
	private void initialize() {
		messageBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            	scrollMessage.setVvalue((Double) newValue);
            }
        });
	}
	
    @FXML
    private void sendMessage() throws IOException {
    	String message = messageToSendField.getText();
    	if(!message.trim().isEmpty()) {
    		if(groupName == null) {
    			new TreadMessageSender(user, socket, message.trim());
        		String pattern = "MM/dd/yyyy HH:mm:ss";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String todayAsString = df.format(today);
        		addSenderMessage(message.trim(), todayAsString);
        		messageToSendField.clear();
    		}else {
    			new MulticastSender(groupeIP, multicastPort, groupName, mySelf, message.trim());
        		String pattern = "MM/dd/yyyy HH:mm:ss";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String todayAsString = df.format(today);
        		addSenderMessage(message.trim(), todayAsString);
        		messageToSendField.clear();
    		}
    		
    	}
    }
    
    @FXML
    private void addUsersToGroup() throws IOException {
    	Stage modal = new Stage();
		modal.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("addUsers.fxml"));
		AnchorPane pseudoPane = fxmlLoader.load();
		AddUsersController controller = fxmlLoader.getController();
		modal.getIcons().add(new Image("file:src/main/resources/Images/chat_icon.png"));
		Scene scene = new Scene(pseudoPane, 600, 400);
		scene.setOnKeyPressed(event -> {
		    if (event.getCode() == KeyCode.ENTER) {
		    	try {
					controller.addUsersGroup();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		});
		modal.setScene(scene);
		modal.setTitle("Add Group Users");
		modal.setResizable(false);
		controller.setStage(modal);
		controller.setGroupNameText(groupName);
		
    	String message = "JoinTheChat".concat("::").concat(groupName).concat("::").concat(groupeIP.getHostAddress()).concat("::").concat(Integer.toString(multicastPort));
    	controller.setMessage(message);
		controller.initializeUsers(App.me.getUserBookManager());
		modal.show();
    }
    
    public void setSendAction() {
    	messageToSendField.setOnKeyPressed(event -> {
		    if (event.isShiftDown() && event.getCode() == KeyCode.ENTER) {
		    	try {
					sendMessage();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		});
    }
    
    public void addReceiverMessage(String message, String date) {
    	HBox hbox1 = new HBox();
        hbox1.setPrefHeight(0.0);
        hbox1.setPrefWidth(600.0);

        TextFlow textFlow = new TextFlow();
        textFlow.setPrefHeight(20.0);
        textFlow.setPrefWidth(200.0);
        textFlow.setStyle("-fx-background-color: #39E75F; -fx-background-radius: 5px;");
        textFlow.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        HBox.setMargin(textFlow, new Insets(0.0, 0.0, 2.0, 0.0));

        Text text = new Text(message);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);

        textFlow.getChildren().add(text);

        hbox1.getChildren().add(textFlow);

        HBox hbox2 = new HBox();
        hbox2.setPrefHeight(3.0);
        hbox2.setPrefWidth(600.0);

        Text text2 = new Text(date);
        text2.setOpacity(0.4);
        text2.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text2.setStrokeWidth(0.0);
        text2.setFont(new Font(8.0));
        HBox.setMargin(text2, new Insets(1.0, 10.0, 5.0, 5.0));

        hbox2.getChildren().add(text2);
        
        messageBox.getChildren().addAll(hbox1, hbox2);
	}
    
    public void addReceiverGroupMessage(String messageAndUser, String date) {
    	HBox hbox1 = new HBox();
        hbox1.setPrefHeight(0.0);
        hbox1.setPrefWidth(600.0);
        
        String[] data = messageAndUser.split("::");

        Label label = new Label(data[1]);
        label.setStyle("-fx-font-weight: 700; -fx-text-fill: #106f24");
        label.setFont(new Font(10.0));

        HBox.setMargin(label, new Insets(2.0, 10.0, 5.0, 5.0));

        TextFlow textFlow = new TextFlow();
        textFlow.setPrefHeight(20.0);
        textFlow.setPrefWidth(200.0);
        textFlow.setStyle("-fx-background-color: #39E75F; -fx-background-radius: 5px;");
        textFlow.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        HBox.setMargin(textFlow, new Insets(0.0, 0.0, 2.0, 0.0));

        Text text = new Text(data[0]);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);

        textFlow.getChildren().add(text);

        hbox1.getChildren().addAll(label, textFlow);

        HBox hbox2 = new HBox();
        hbox2.setPrefHeight(3.0);
        hbox2.setPrefWidth(600.0);

        Text text2 = new Text(date);
        text2.setOpacity(0.4);
        text2.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text2.setStrokeWidth(0.0);
        text2.setFont(new Font(8.0));
        HBox.setMargin(text2, new Insets(1.0, 10.0, 5.0, 5.0));

        hbox2.getChildren().add(text2);
        
        messageBox.getChildren().addAll(hbox1, hbox2);
	}
    
    private void addSenderMessage(String message, String date) {
    	HBox hbox1 = new HBox();
        hbox1.setPrefHeight(0.0);
        hbox1.setPrefWidth(600.0);
        hbox1.setAlignment(Pos.CENTER_RIGHT);

        TextFlow textFlow = new TextFlow();
        textFlow.setPrefHeight(20.0);
        textFlow.setPrefWidth(200.0);
        textFlow.setStyle("-fx-background-color: #45B6FE; -fx-background-radius: 5px;");
        textFlow.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        HBox.setMargin(textFlow, new Insets(0.0, 0.0, 2.0, 0.0));

        Text text = new Text(message);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);

        textFlow.getChildren().add(text);

        hbox1.getChildren().add(textFlow);

        HBox hbox2 = new HBox();
        hbox2.setPrefHeight(3.0);
        hbox2.setPrefWidth(600.0);
        hbox2.setAlignment(Pos.CENTER_RIGHT);

        Text text2 = new Text(date);
        text2.setOpacity(0.4);
        text2.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text2.setStrokeWidth(0.0);
        text2.setFont(new Font(8.0));
        HBox.setMargin(text2, new Insets(1.0, 5.0, 5.0, 10.0));

        hbox2.getChildren().add(text2);
        
        messageBox.getChildren().addAll(hbox1, hbox2);
	}
    
    public void chargeOldMessages(List<String> messages) {
    	for(String message : messages) {
    		String[] data = message.split("::");
    		if(data[1].equals("0")) {
    			addSenderMessage(data[2], data[3]);
    		}else {
    			addReceiverMessage(data[2], data[3]);
    		}
    	}
    }

    @FXML
    public void sendFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images", "*.jpg", "*.jpeg","*.png", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
            	System.out.println(file.getName());
//                Socket socket = new Socket("hostname", port);
//                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//                FileInputStream fis = new FileInputStream(file);
//                byte[] buffer = new byte[(int)file.length()];
//                int bytesRead = fis.read(buffer);
//                dos.write(buffer, 0, bytesRead);
//                fis.close();
//                dos.close();
//                socket.close();
//                System.out.println("File sent successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
	public OnlineUser getUser() {
		return user;
	}

	public void setUser(OnlineUser user) {
		this.user = user;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void updateOnlineUser(OnlineUser user) {
		setUser(user);
		setUserPseudoText(user.getPseudo());
	}

	public InetAddress getGroupeIP() {
		return groupeIP;
	}

	public void setGroupeIP(InetAddress groupeIP) {
		this.groupeIP = groupeIP;
	}

	public int getMulticastPort() {
		return multicastPort;
	}

	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public User getMySelf() {
		return mySelf;
	}

	public void setMySelf(User mySelf) {
		this.mySelf = mySelf;
	}
}
