
import java.awt.Dimension;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.LineUnavailableException;

import com.github.sarxos.webcam.Webcam;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CamerasLayout extends VBox {
	public static Webcam webcam;
	private static FlowPane userImg = new FlowPane();
	private HBox controls = new HBox(10);
	AudioRecieve ar;
	AudioSender as;
	VideoRecieve vr;
	boolean CamStatus = true;
	VideoFrames vf;
	ImageView iv; 
	
	public CamerasLayout(Room r) throws IOException {
		
		userImg.setVgap(10);
		userImg.setHgap(10);
		//setPadding(new Insets(10));
		
		Button Cam = new Button("Cam");
		Button Mic = new Button("Mic");
		Button Leave = 	new Button("Leave");
		controls.getChildren().addAll(Cam,Mic, Leave);
		
		controls.setAlignment(Pos.BOTTOM_CENTER);
		userImg.prefWidthProperty().bind(this.prefWidthProperty());
		userImg.prefHeightProperty().bind(this.prefHeightProperty());
		
		
		Cam.setOnAction(e -> {
			if(CamStatus) {
				vf.setFlag(false);
				webcam.close();
				iv.setImage(new Image("user.png"));
				ChatSender.SendCmd("camoff");
				CamStatus = false;
			}else {
				// TODO DONT USE SUSPEND/RESUME USE WAIT/NOTIFY
				webcam.open();
				ChatSender.SendCmd("camon");
				CamStatus = true;
				try {
					vf = new VideoFrames(iv, r);
					vf.setFlag(true);
				    vf.start();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (SocketException e1) {
					e1.printStackTrace();
				}
		       
			}
			
		});
		
		Leave.setOnAction(e -> {
			try {
				
				MakeRequest.postRequest("/leaveroom/"+r.meetid, App.getToken());
			} catch (IOException | InterruptedException e2) {
				e2.printStackTrace();
			}
			vf.setFlag(false);
			vr.setFlag(false);
			ar.setFlag(false);
			as.setFlag(false);
			ChatReciever.setFlag(false);
			
			
			ChatSender.SendCmd("leaveroom");
			userImg = new FlowPane();
			
			String response;
			try {
				response = MakeRequest.postRequest2("/attendance/"+r.meetid, App.getToken());
				ViewAttendance va = new ViewAttendance(response);
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			App.setMainView();
		});

		
		setStyle("-fx-border-width: 5px; -fx-border-color: black; ");
			getChildren().addAll(userImg, controls);
			try {
				StartVideo(r);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
				
	}

	
	public void StartVideo(Room room) throws IOException, InterruptedException, LineUnavailableException {
			Thread.sleep(1000);
	    	webcam = Webcam.getDefault();
	        webcam.setViewSize(new Dimension(320,240));
	        webcam.open();
	        iv = new ImageView();
	        userImg.getChildren().add(iv);
	        vr = new VideoRecieve(room);
	        vr.setFlag(true);
	        vr.start();
	        vf = new VideoFrames(iv, room);
	        vf.setFlag(true);
	        vf.start();
	        ar = new AudioRecieve(room);
	        ar.setFlag(true);
	        ar.start();
	        as = new AudioSender(room);
	        as.setFlag(true);
	        as.start();
	    }
	
	
	 public static void removeUser(ImageView img) {
		 userImg.getChildren().remove(img);
	 }
	 public static void addUser(ImageView img) {
		 	
	    	userImg.getChildren().add(img);
	 }
	    
	
}
