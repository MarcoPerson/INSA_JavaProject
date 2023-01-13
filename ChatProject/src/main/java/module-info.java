module MarcoWalter.ChatProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
	requires javafx.base;
	requires javafx.graphics;
	requires java.desktop;

    opens MarcoWalter.ChatProject to javafx.fxml;
    exports MarcoWalter.ChatProject;
}
