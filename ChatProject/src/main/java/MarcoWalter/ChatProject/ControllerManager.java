package MarcoWalter.ChatProject;

import java.util.List;

import MarcoWalter.ChatProject.Models.OnlineUser;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

public class ControllerManager {
	public void setconnectionMessageText(LoginController controller, String text) {
		if (controller == null || controller.connectionMessage == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.connectionMessage.setText(text);
			}
		});
	}

	public void setDiscussionScene(HomeController controller, AnchorPane scene) {
		if (controller == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.setDiscussion(scene);
			}
		});
	}

	public void addMessageIntoScrollPane(MessageController controller, String message, String date, String type) {
		if (controller == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(type.equals("group")) {	
					controller.addReceiverGroupMessage(message, date);
				}else {					
					controller.addReceiverMessage(message, date);
				}
			}
		});
	}
	
	public void setSendButtonAction(ModelController controller) {
		if (controller == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.setSendAction();
			}
		});
	}

	public void disableSendButtonAction(ModelController controller) {
		if (controller == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.disableSendAction();
			}
		});
	}

	public void updateHomeTitle() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				App.getStage().setTitle("Home - " + App.me.getPseudo());
			}
		});
	}

	public void updateTableList(HomeController controller) {
		if (controller == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.updateTableList();
			}
		});
	}

	public void updateOnlineUser(MessageController controller, OnlineUser user) {
		if (controller == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.updateOnlineUser(user);
			}
		});
	}

	public void manageDisconnectedUser(HomeController controller, OnlineUser newuser) {
		if (controller == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.manageDisconnectedUser(newuser);
			}
		});

	}
	
	public void showNotification(HomeController controller, String message) {
		if (controller == null) {
			System.out.println("Controller or connectionMessage object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.showNotification(message);
			}
		});

	}

	public void setOldMessages(MessageController controller, List<String> messages) {
		if (controller == null) {
			System.out.println("Controller object is null. Text cannot be set.");
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.chargeOldMessages(messages);
			}
		});
		
	}
}
