module MarcoWalter.ChatProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
	requires javafx.base;

    opens MarcoWalter.ChatProject to javafx.fxml;
    exports MarcoWalter.ChatProject;
}
