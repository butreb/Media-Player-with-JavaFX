package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	
	Boolean controlPressed = false;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			
			
			primaryStage.setTitle("bertub Media Player");
			Image icon = new Image("bplayericon.png");
			primaryStage.getIcons().add(icon);
			
			Controller control = loader.getController();
			
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					// TODO Auto-generated method stub
					
					switch(event.getCode()){
					
					case F11:
						control.fullscreen();
						break;
					case SPACE:
						
						control.playPauseBtn();
						
						
						break;
									
					default:
						break;
						
						
				
					}
				}	
			});
			
			 scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
				 
				 
		            if (event.getCode() == KeyCode.CONTROL) {
		                 controlPressed = true;
		            } else if (controlPressed && event.getCode() == KeyCode.O) {
		                
		                control.open();
		            }
		        });
			 scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
		            if (event.getCode() == KeyCode.CONTROL) {
		                controlPressed = false;
		            }
		        });
			 
			 control.toFrontPane();
			 control.btnImage();
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
