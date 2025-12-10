module com.lamentofashes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens com.lamentofashes.controllers to javafx.fxml;
    opens com.lamentofashes to javafx.fxml;
    exports com.lamentofashes;
}
