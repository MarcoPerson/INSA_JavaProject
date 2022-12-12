package MarcoWalter.ChatProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

import MarcoWalter.ChatProject.Models.User;
import MarcoWalter.ChatProject.UdpControllers.UserSocketUDP;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
    	try {
    		User walter = new User(1);
            Scanner pseudoChooser = new Scanner(System.in);
            UserSocketUDP walSocketUDP = new UserSocketUDP(walter);
            boolean agreed = false;
            do {
                System.out.print("Choose a new Pseudo : ");
                walter.modifyPseudo(pseudoChooser.nextLine());
                walter.connectToNetwork(walSocketUDP);
                agreed = walSocketUDP.waitForAggrement();
            } while (agreed == false);

            walSocketUDP.broadcast(walter.getId(), walter.getPseudo(), "newUser");
            Thread reception = new Thread(() -> walSocketUDP.receiveMessage());
            reception.start();

            pseudoChooser.close();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
        launch();
    }

}