package MarcoWalter.ChatProject.Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private  String userId;
    private  String password;
    private String databaseName;

    public DataBase(String _userId, String _password, String _databaseName){
        userId = _userId;
        password = _password;
        databaseName = _databaseName;
    }

public void createNewDataBase(String userId, String password){
    String url = "jdbc:sqlite:" + databaseName;
    
    try {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection(url,userId,password);
        if (conn != null) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("The user name is " + userId + " " + password);
            System.out.println("A new database has been created.");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public  Connection connect(String userId, String password){
    Connection conn = null;
    try{
        String url = "jdbc:sqlite:" + databaseName;
        conn = DriverManager.getConnection(url, userId, password);
        System.out.println("The user name is " + conn.getMetaData().getUserName() + " " + userId + " " + password);
        System.out.println("Connection to SQLite has been established."); 
    } catch (Exception e){
        e.printStackTrace();
    } /*finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }*/
    return conn;
}

public String getDatabaseName() {
	return databaseName;
}
}

