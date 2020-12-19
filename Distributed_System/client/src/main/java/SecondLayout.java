import java.awt.Dimension;
import java.io.IOException;

import com.github.sarxos.webcam.Webcam;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SecondLayout extends VBox {
	public static int MYID = -1;
	private Label JoinOrCreateRoom = new Label("Join Or Create Room");
	private TextField enterRoomID =  new TextField();
	private VBox vb = new VBox(40);
	public static Button createRoom = new Button("Create");
	private Button joinRoom = new Button("Join");
	private Button logout = new Button("Logout");
	public SecondLayout() {
		
		setPadding(new Insets(40));
		
		createRoom.setOnAction( new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Gson g = new Gson();
				try {
					response res = MakeRequest.postRequest("/createroom", App.getToken());
					if(res != null && res.getStatus().contentEquals("OK")) {
						Room p = g.fromJson(res.getResult(), Room.class);
						System.out.println("Create");
						System.out.println(p.toString());
						ChatLayout  chatayout = new ChatLayout(p);
						CamerasLayout cameraslayout=new CamerasLayout(p); 
	    				App.primaryStage.setScene(new Scene(new MeetingLayout(chatayout, cameraslayout), 1600, 900));
					}else {
						Alert a = new Alert(AlertType.ERROR);
						a.setHeaderText(res.getError());
						a.show();
						App.setLoginView();
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
        
		
		
		
		joinRoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {	
            	JoinRoom();
            }
        });
		
		
		
		enterRoomID.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER) {
					JoinRoom();
				}
			}
		});
		
		HBox hb = new HBox();
		
		hb.getChildren().add(logout);		
		hb.setAlignment(Pos.BASELINE_RIGHT);
		vb.getChildren().addAll(JoinOrCreateRoom,createRoom,enterRoomID,joinRoom, hb);
		vb.setMaxSize(400,400);
		setAlignment(Pos.CENTER);
		createRoom.setAlignment(Pos.CENTER);
		JoinOrCreateRoom.setAlignment(Pos.CENTER);
		enterRoomID.setAlignment(Pos.CENTER);
		joinRoom.setAlignment(Pos.CENTER);
		vb.setAlignment(Pos.CENTER);
		enterRoomID.setPrefWidth(50);
		createRoom.setStyle("-fx-background-color:#b19cd9;\n" + 
				"  -fx-text-fill:white;\n" + 
				"-fx-background-radius: 5;\n"+
				"  -fx-pref-height:50px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:5px;\n" + 
				"  -fx-pref-width:100px; ");
		
		logout.setStyle("-fx-background-color:#b19cd9;\n" + 
				"  -fx-text-fill:white;\n" + 
				"-fx-background-radius: 5;\n"+
				"  -fx-pref-height:37px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:5px;\n" + 
				"  -fx-pref-width:80px; ");
		
		JoinOrCreateRoom.setStyle(
				"  -fx-text-fill:#b19cd9;\n" + 
				"-fx-background-radius: 5;\n"+
				"-fx-font-size:25;\n"+
				"  -fx-pref-height:40px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:5px;\n" + 
				"  -fx-pref-width:300px; ");
		enterRoomID.setStyle("-fx-background-color:#b19cd9;\n" + 
				"  -fx-text-fill:white;\n" + 
				"-fx-background-radius: 5;\n"+
				"  -fx-pref-height:50px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:5px;\n" + 
				
				"  -fx-max-width:300px; ");
		joinRoom.setStyle("-fx-background-color:#b19cd9;\n" + 
				"  -fx-text-fill:white;\n" + 
				"-fx-background-radius: 5;\n"+
				"  -fx-pref-height:50px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:5px;\n" + 
				"  -fx-pref-width:100px; ");
		
		getChildren().addAll(vb);
		setMaxHeight(400);
		setMaxWidth(400);
		
		
		logout.setOnAction(e->{
			
			user logoutData = user.readToken();
			try {
				response res = MakeRequest.postRequest("/logout",logoutData);
				if(res.getStatus().contentEquals("OK")) {
					user.WriteToken(null, null);
					App.setLoginView();
				}else {
					user.WriteToken(null, null);
					Alert a = new Alert(AlertType.ERROR);
					a.setHeaderText(res.getError());
					a.show();
					App.setLoginView();
				}
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
			
		});
	}

	
	 public void JoinRoom() {
		 Gson g = new Gson();
			try {
				response res = MakeRequest.postRequest("/joinroom/"+enterRoomID.getText(), App.getToken());
				if(res != null && res.getStatus().contentEquals("OK")) {
					Room p = g.fromJson(res.getResult(), Room.class);
					System.out.println("Join");
					System.out.println(p.toString());
	
					CamerasLayout cameraslayout=new CamerasLayout(p);
					ChatLayout  chatayout = new ChatLayout(p);
					App.primaryStage.setScene(new Scene(new MeetingLayout(chatayout, cameraslayout), 1600, 900));			 
				}else {
					Alert a = new Alert(AlertType.ERROR);
					a.setHeaderText(res.getError());
					a.show();
					App.setLoginView();
				}
			 
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	 }
	 
	 
	 

}
