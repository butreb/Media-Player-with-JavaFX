module Media_Player {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
}
