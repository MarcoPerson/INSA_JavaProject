package MarcoWalter.ChatProject.DatabaseControllers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import MarcoWalter.ChatProject.Models.DataBase;

public class DbControllers {
    private DataBase db;
    private Connection conn;

    public DbControllers(DataBase _db, String _userId, String _password) {
        db = _db;
        conn = db.connect(_userId, _password);
    }

    public void createNewTable() {

        String url = "jdbc:sqlite:" + db.getDatabaseName();
        String resquest = "CREATE TABLE userMessages ("
                + " idMessage INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "idChater INTEGER NOT NULL,"
                + "type INTEGER NOT NULL,"
                + "message TEXT,"
                + "time TEXT"
                + " )";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(resquest);
            System.out.println("***Table has beeen created***");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertLine(int idChater, int type, String message, String date) {
        String resquest = "INSERT INTO userMessages (idChater, type, message, time)"
                + "VALUES(?,?,?,?)";
        try {
            PreparedStatement statement = conn.prepareStatement(resquest);
            statement.setInt(1, idChater);
            statement.setInt(2, type);
            statement.setString(3, message);
            statement.setString(4, date);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public List<String> getMessageWith(int _idChater) {
        List<String> messages = new ArrayList<>();
        String resquest = "SELECT idMessage, type, message, time FROM userMessages WHERE idChater = "
                + Integer.toString(_idChater);
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(resquest);
            while (result.next()) {
                String entity = Integer.toString(result.getInt("idMessage")).concat("::").concat(Integer.toString(result.getInt("type"))).concat("::").concat(result.getString("message"))
                        .concat("::").concat(result.getString("time"));
                messages.add(entity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }
}
