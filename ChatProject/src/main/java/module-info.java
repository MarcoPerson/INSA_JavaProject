module MarcoWalter.ChatProject {
    requires javafx.controls;
    requires javafx.fxml;

    opens MarcoWalter.ChatProject to javafx.fxml;
    exports MarcoWalter.ChatProject;
}
