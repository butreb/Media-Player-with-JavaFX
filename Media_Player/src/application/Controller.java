package application;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.VideoTrack;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import javafx.stage.Stage;

public class Controller {

	@FXML
	private MenuBar menuBar;
	
	@FXML
	private MediaView mediaView;
	
	private File file;
	
	private Media media;
	private MediaPlayer mediaPlayer;
	
	
	private Stage stage;
	
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private StackPane stackPane;
	
	@FXML
	private AnchorPane tabAnchorPane;
	@FXML
	private Label currentTimeLabel;
	@FXML
	private Label TimeLabel;
	
	@FXML
	private Slider videoSlider;
	
	@FXML
    private Slider volumeSlider;
	
	@FXML
	private ListView<String> mediaList;
	public int counter =0;
	
	@FXML
	private AnchorPane listAnchorPane;
	
	@FXML
	private ImageView mediaForward;
	@FXML
	private ImageView mediaBackward;
	
	@FXML
	private Button myButton;
	@FXML
	private Button myButton1;
	@FXML
	private Button myButton2;
	
	private ImageView imageView;
	
	public void open() {
		try {
		FileChooser choose = new FileChooser();
		choose.setTitle("Select a .mp4 file");
		choose.getExtensionFilters().addAll(
			    new ExtensionFilter("MP4 Files (*.mp4)", "*.mp4"),
			    new ExtensionFilter("MP3 Files (*.mp3)", "*.mp3"),
			    new ExtensionFilter("All Files", "*.*")
			);

		
		file = choose.showOpenDialog(null);
		
		if (file!=null) {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				 mediaPlayer.dispose(); 
			}
			
			mediaList.setOnMouseClicked(event -> {				
			if (mediaPlayer!= null) {
				mediaPlayer.stop(); 
				mediaPlayer.dispose();
					
				}
				String path = mediaList.getSelectionModel().getSelectedItem();
				File selectedFile = new File(path);
				 if (selectedFile.exists()) {
			         Media media = new Media(selectedFile.toURI().toString());
			         
			    mediaPlayer = new MediaPlayer(media); 
			     mediaView.setMediaPlayer(mediaPlayer);
			     	
			     mediaPlayer.play(); 
			     mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                     Duration currentTime = newValue;
                     double currentTimeSeconds = currentTime.toSeconds();

                   
                     videoSlider.setValue(currentTimeSeconds);

                   
                     LocalTime localTime = LocalTime.ofSecondOfDay((long) currentTimeSeconds);
                     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
                     String formattedTime = formatter.format(localTime);
                     currentTimeLabel.setText(formattedTime);
                 });
			     
			     
			     mediaPlayer.setOnReady(() -> {
			    	 Scene scene = mediaList.getScene();
		                if (scene != null) {
		                    scene.getRoot().requestFocus();
		                }
		                
		                Duration totalDuration = mediaPlayer.getMedia().getDuration();
                        double totalDurationSeconds = totalDuration.toSeconds();

                        videoSlider.setMin(0);
                        videoSlider.setMax(totalDurationSeconds);

                        LocalTime localTime = LocalTime.ofSecondOfDay((long) totalDurationSeconds);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
                        String formattedTime = formatter.format(localTime);
                        TimeLabel.setText(formattedTime);
		            });
			     

                 mediaPlayer.setOnEndOfMedia(() -> {
                     mediaPlayer.stop();
                     videoSlider.setValue(0);
                 });
                 
                 videoSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                     if (videoSlider.isValueChanging()) {
                         mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                     }
                 });

                 mediaPlayer.play();
			     
			     }
			});
		    mediaList.setOnKeyPressed(event -> {
		        if (event.getCode() == KeyCode.SPACE && mediaPlayer != null) {
		            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
		                mediaPlayer.pause();
		            } else {
		                mediaPlayer.play();
		            }
		            event.consume();
		        }
		    });
		

			videoSlider.setValue(0);
			media = new Media(file.toURI().toString());
			
			anchorPane.widthProperty().addListener((observable, oldValue, newValue) -> {
	            mediaView.setFitWidth(newValue.doubleValue());
	        });

	        anchorPane.heightProperty().addListener((observable, oldValue, newValue) -> {
	            mediaView.setFitHeight(newValue.doubleValue());
	        });
			
			mediaPlayer = new MediaPlayer(media);
			mediaView.setMediaPlayer(mediaPlayer);
			
			mediaPlayer.play();
			mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
			    Duration currentTime = newValue;
			    LocalTime localTime = LocalTime.ofSecondOfDay((long) currentTime.toSeconds());
			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
			    String formattedTime = formatter.format(localTime);
			    currentTimeLabel.setText(formattedTime);
			    
			    if (!videoSlider.isValueChanging()) {
                    videoSlider.setValue(currentTime.toSeconds());
                }
			});
			
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setOnReady(() -> {
			 
			    Duration totalDuration = mediaPlayer.getMedia().getDuration();
			    LocalTime localTime = LocalTime.ofSecondOfDay((long) totalDuration.toSeconds());
			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
			    String formattedTime = formatter.format(localTime);
			    TimeLabel.setText(formattedTime);
			    
			    videoSlider.setMin(0);
                videoSlider.setMax(totalDuration.toSeconds());
                videoSlider.setValue(0);
			});
			
			  mediaPlayer.setOnEndOfMedia(() -> {
	                mediaPlayer.stop();
	                videoSlider.setValue(0);
	            });
			  
			  videoSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
	                if (videoSlider.isValueChanging()) {
	                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
	                }
	                
	            });
			 
			
		mediaList.getItems().add(file.getPath());
		mediaList.getSelectionModel().select(file.getPath());
		volumeSlider.setValue(mediaPlayer.getVolume()*100);
			counter = 0;
			
		
		}
		
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public void fullscreen() {
		try {
			
		
		menuBar.setVisible(false);
		 anchorPane.widthProperty().addListener((observable, oldValue, newValue) -> {
	            mediaView.setFitWidth(newValue.doubleValue());
	        });

	        anchorPane.heightProperty().addListener((observable, oldValue, newValue) -> {
	            mediaView.setFitHeight(newValue.doubleValue());
	        });
	        anchorPane.setTopAnchor(stackPane, 0.0);
	        stackPane.setPadding(new Insets(0));
	        stackPane.prefWidthProperty().bind(anchorPane.widthProperty());
	        stackPane.prefHeightProperty().bind(anchorPane.heightProperty());
	        
		stage = (Stage) anchorPane.getScene().getWindow();
		stage.setFullScreen(true);
		
		
		
		stage.addEventHandler(KeyEvent.KEY_PRESSED, event->{
			
			if(event.getSource() == KeyCode.ESCAPE) {
				
				menuBar.setVisible(true);
				 anchorPane.widthProperty().addListener((observable, oldValue, newValue) -> {
			            mediaView.setFitWidth(newValue.doubleValue());
			        });

			        anchorPane.heightProperty().addListener((observable, oldValue, newValue) -> {
			            mediaView.setFitHeight(newValue.doubleValue());
			        });
			        anchorPane.setTopAnchor(stackPane, 27.0);
			        stackPane.setPadding(new Insets(22.0,0,0,0));
			        stackPane.prefWidthProperty().bind(anchorPane.widthProperty());
			        stackPane.prefHeightProperty().bind(anchorPane.heightProperty());
			}	
		});
		
		stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
            	menuBar.setVisible(true);
            	 anchorPane.widthProperty().addListener((observable, oldValue, newValue) -> {
                     mediaView.setFitWidth(newValue.doubleValue());
                 });

                 anchorPane.heightProperty().addListener((observable, oldValue, newValue) -> {
                     mediaView.setFitHeight(newValue.doubleValue());
                 });
                 anchorPane.setTopAnchor(stackPane, 27.0);
                 stackPane.setPadding(new Insets(22.0,0,0,0));
                 stackPane.prefWidthProperty().bind(anchorPane.widthProperty());
                 stackPane.prefHeightProperty().bind(anchorPane.heightProperty());
                 
            }
        });
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void initialize() {
        anchorPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            mediaView.setFitWidth(newValue.doubleValue());
        });

        anchorPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            mediaView.setFitHeight(newValue.doubleValue());
        });
    }
	
	
	public void showPane() {
		tabAnchorPane.setOpacity(1);
		
	}
	
public void hidePane() {
		
	tabAnchorPane.setOpacity(0);
	}
public void toFrontPane() {
	tabAnchorPane.toFront();
	listAnchorPane.toFront();
	
}

public void drag() {
	try {
		 mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
	           if (!videoSlider.isValueChanging()) {
	        	   videoSlider.setValue(newValue.toSeconds());
	           }
	       });
	   videoSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
           if (videoSlider.isValueChanging()) {
               mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
               
           }
       });
	}catch (Exception e) {
		// TODO: handle exception
	}
}
public void slidermouseClick() {
	try {
		double seekTime = videoSlider.getValue();
	    mediaPlayer.seek(Duration.seconds(seekTime));
	} catch (Exception e) {
		// TODO: handle exception
	}
	
}
public void forwardBackward(KeyEvent event) {
	try {
	
	if (event.getCode() == KeyCode.RIGHT) {
		double seekTime = videoSlider.getValue();
		double newValue = seekTime;
		videoSlider.setValue(newValue);
		mediaPlayer.seek(Duration.seconds(newValue));
	}
	else if (event.getCode()==KeyCode.LEFT) {
		double seekTime = videoSlider.getValue();
		double newValue = seekTime;
		videoSlider.setValue(newValue);
		mediaPlayer.seek(Duration.seconds(newValue));
	}
	
    
	}catch (Exception e) {
		// TODO: handle exception
	}

}

public void setVolumeDrag() {
	try {
		
		volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100; 
            mediaPlayer.setVolume(volume);
        });
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	
}

public void hideList() {
	
	listAnchorPane.setOpacity(0);
}
public void showList() {
	listAnchorPane.setOpacity(1);
	
}
@FXML
private void clickForward() {
	try {
    int currentIndex = mediaList.getSelectionModel().getSelectedIndex();
    int nextIndex = currentIndex + 1;
    int lastIndex = mediaList.getItems().size() - 1;
    
    if (nextIndex > lastIndex) {
        nextIndex = 0;
    }
        
        mediaList.scrollTo(nextIndex);
        mediaList.getSelectionModel().select(nextIndex);
        openSelectedMedia();
     /*  
	if (mediaPlayer!= null) {
		mediaPlayer.stop(); 
		mediaPlayer.dispose();
			
		}
        String path = mediaList.getSelectionModel().getSelectedItem();
		File selectedFile = new File(path);
		 if (selectedFile.exists()) {
	         Media media = new Media(selectedFile.toURI().toString());
	         
	         mediaPlayer = new MediaPlayer(media); 
	     mediaView.setMediaPlayer(mediaPlayer);
	     	
	     mediaPlayer.play(); 
        
		 }
		 */
}catch (Exception e) {
	// TODO: handle exception
}
}

@FXML
private void clickBackward() {
	try {
	 int currentIndex = mediaList.getSelectionModel().getSelectedIndex();
	    int nextIndex = currentIndex -1;
	    int lastIndex = mediaList.getItems().size() - 1;
	    
	    int firstIndex = 0;
	    
	    if (nextIndex < firstIndex) {
            nextIndex = lastIndex;
        }
	        mediaList.scrollTo(nextIndex);
	        mediaList.getSelectionModel().select(nextIndex);
	        openSelectedMedia();
	  /*    
		if (mediaPlayer!= null) {
			mediaPlayer.stop(); 
			mediaPlayer.dispose();
				
			}
	        String path = mediaList.getSelectionModel().getSelectedItem();
			File selectedFile = new File(path);
			 if (selectedFile.exists()) {
		         Media media = new Media(selectedFile.toURI().toString());
		         
		         mediaPlayer = new MediaPlayer(media); 
		     mediaView.setMediaPlayer(mediaPlayer);
		     	
		     mediaPlayer.play(); 
			 }
			 */
	}catch (Exception e) {
		// TODO: handle exception
	}
}
public void btnImage() {
	
	Image image = new Image("media-export.png");
	ImageView imageView = new ImageView(image);
	imageView.setPreserveRatio(true);
	
	imageView.setFitWidth(myButton.getWidth());
	imageView.setFitHeight(myButton.getHeight());
	
	myButton.setGraphic(imageView);
	
	Image imagefrw = new Image("forward-export.png");
	ImageView imageViewfrw = new ImageView(imagefrw);
	imageViewfrw.setPreserveRatio(true);
	
	imageViewfrw.setFitWidth(myButton.getWidth());
	imageViewfrw.setFitHeight(myButton.getHeight());
	
	myButton1.setGraphic(imageViewfrw);
	
	Image imagebck = new Image("backward-export.png");
	ImageView imageViewbck = new ImageView(imagebck);
	imageViewbck.setPreserveRatio(true);
	
	imageViewbck.setFitWidth(myButton.getWidth());
	imageViewbck.setFitHeight(myButton.getHeight());
	
	myButton2.setGraphic(imageViewbck);
	
	
}
@FXML
public void playPauseBtn() {
	try {
		if (counter == 0) {
          
            Image image = new Image("mediaStop.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(25); 
            imageView.setFitHeight(24); 
            myButton.setGraphic(imageView);

            
            mediaPlayer.pause();
          
            counter = 1;
        } else if (counter == 1) {
            
            Image image = new Image("media-export.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(25);
            imageView.setFitHeight(24);
            myButton.setGraphic(imageView);

           
            mediaPlayer.play();

        
            counter = 0;
        }
		
		
	} catch (Exception e) {
		// TODO: handle exception
	}

}
private void openSelectedMedia() {
    try {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        String path = mediaList.getSelectionModel().getSelectedItem();
        File selectedFile = new File(path);
        if (selectedFile.exists()) {
            Media media = new Media(selectedFile.toURI().toString());

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.play();

            mediaPlayer.setOnReady(() -> {
                Duration totalDuration = mediaPlayer.getMedia().getDuration();
                LocalTime localTime = LocalTime.ofSecondOfDay((long) totalDuration.toSeconds());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
                String formattedTime = formatter.format(localTime);
                TimeLabel.setText(formattedTime);

                videoSlider.setMin(0);
                videoSlider.setMax(totalDuration.toSeconds());
            });

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                Duration currentTime = newValue;
                LocalTime localTime = LocalTime.ofSecondOfDay((long) currentTime.toSeconds());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
                String formattedTime = formatter.format(localTime);
                currentTimeLabel.setText(formattedTime);

                if (!videoSlider.isValueChanging()) {
                    videoSlider.setValue(currentTime.toSeconds());
                }
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.stop();
                videoSlider.setValue(0);
            });

            videoSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (videoSlider.isValueChanging()) {
                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                }
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
