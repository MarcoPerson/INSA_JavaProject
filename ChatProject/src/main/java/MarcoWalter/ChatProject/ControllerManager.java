package MarcoWalter.ChatProject;

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
               controller.setDiscussion(scene);;
           }
       });
   }
}
