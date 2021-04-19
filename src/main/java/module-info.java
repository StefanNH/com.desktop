module mp.com.desktop {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;

    opens mp.com.desktop to javafx.fxml;
    exports mp.com.desktop;
}
