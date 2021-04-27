module mp.com.desktop {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires json.simple;

    opens mp.com.desktop to javafx.fxml;
    exports mp.com.desktop;
}
