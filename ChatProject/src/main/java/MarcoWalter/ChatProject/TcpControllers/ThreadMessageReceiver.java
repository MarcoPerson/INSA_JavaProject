package MarcoWalter.ChatProject.TcpControllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import MarcoWalter.ChatProject.App;
import MarcoWalter.ChatProject.ControllerManager;
import MarcoWalter.ChatProject.MessageController;
import MarcoWalter.ChatProject.DatabaseControllers.DbControllers;
import MarcoWalter.ChatProject.Models.DataBase;
import MarcoWalter.ChatProject.Models.OnlineUser;

public class ThreadMessageReceiver extends Thread {
	private OnlineUser user;
	private Socket socket;
	DataInputStream receive;
	DataBase dataBase;
	DbControllers dbConn;

	public ThreadMessageReceiver(OnlineUser _user, Socket _socket) {
		user = _user;
		socket = _socket;
		try {
			receive = new DataInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataBase = new DataBase("", "", "Chat.db");
		dbConn = new DbControllers(dataBase, "", "");
		start();
	}

	public void run() {
		boolean bool = true;
		while (bool) {
			bool = receiveMessage();
		}
	}

	public boolean receiveMessage() {
		byte[] buffer = new byte[1000000];

		try {
			byte messageType = receive.readByte();
			if (messageType == 1) {
				String message = "nouveau_fichier";
				BufferedImage image = ImageIO.read(receive);
				File outputFile = new File(message);
				ImageIO.write(image, "png", outputFile);
				String pattern = "MM/dd/yyyy HH:mm:ss";
				DateFormat df = new SimpleDateFormat(pattern);
				Date today = Calendar.getInstance().getTime();
				String todayAsString = df.format(today);
				dbConn.insertLineIntoUserMessages(user.getId(), 1, "[image] : " + message, todayAsString);

				MessageController controller = App.discussionControllers.get(user.getId());
				new ControllerManager().addMessageIntoScrollPane(controller, "[image] : " + message, todayAsString,
						"single");
			} else if(messageType == 0) {
				String message = receive.readUTF();
				String pattern = "MM/dd/yyyy HH:mm:ss";
				DateFormat df = new SimpleDateFormat(pattern);
				Date today = Calendar.getInstance().getTime();
				String todayAsString = df.format(today);
				dbConn.insertLineIntoUserMessages(user.getId(), 1, message, todayAsString);

				MessageController controller = App.discussionControllers.get(user.getId());
				new ControllerManager().addMessageIntoScrollPane(controller, message, todayAsString, "single");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.toString().equals("java.net.SocketException: Socket closed")) {
				return false;
			}
		}
		return true;
	}

	public static boolean isText(String received) {
		// check if received data is text
		// you can use regex or other methods to check for text file type
		return received.matches("^[a-zA-Z0-9\\s\\n\\r.,:;!?]+$");
	}

	public static boolean isImage(String received) {
		// check if received data is an image
		// you can use regex or other methods to check for image file type
		String[] imageExtensions = { "jpg", "jpeg", "png", "gif" };
		for (String ext : imageExtensions) {
			if (received.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
}
