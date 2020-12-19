
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class FirstScene extends VBox{
	private Label label = new Label("Login");
	private TextField enteremail =  new TextField();
	private PasswordField enterpass =  new PasswordField();
	private VBox vb = new VBox(40);
	private Button enter = new Button("ENTER");
	private Button register = new Button("REGISTER");
	private HBox hb = new HBox(20);
	
	public FirstScene() {
		
		//String appdata = System.getenv("APPDATA");
		
		//System.out.println(appdata);
		
		setPadding(new Insets(50));
		
		enter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//App.username = enteremail.getText().toString();
            	App.primaryStage.setScene(new Scene(new SecondLayout(),400,400));
            }
        });
		
		
		enteremail.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER && enteremail.getText().toString().replace(" ", "") != "") {
	            	//App.username = enteremail.getText().toString();
	            	App.primaryStage.setScene(new Scene(new SecondLayout(),400,400));
				}
			}
		});
		
		String style = "-fx-background-color:#b19cd9;\n" + 
				"-fx-text-fill:white;\n" + 
				"-fx-prompt-text-fill: white;\n"+
				"-fx-background-radius: 5;\n"+
				"-fx-pref-height:50px;\n" +
				"-fx-font-weight: bold;\n" +
				"-fx-padding:5px;\n" + 
				" -fx-max-width:300px; ";
		
		String style1 = "-fx-background-color:#b19cd9;\n" + 
				"  -fx-text-fill:white;\n" + 
				"-fx-background-radius: 5;\n"+
				"  -fx-pref-height:50px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:5px;\n" + 
				"  -fx-pref-width:100px; ";
		
		
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(enter, register);
		vb.getChildren().addAll(label,enteremail,enterpass, hb);
		vb.setMaxSize(400,400);
		setAlignment(Pos.CENTER);
		label.setAlignment(Pos.CENTER);
		//enteremail.setAlignment(Pos.CENTER);
		enter.setAlignment(Pos.CENTER);
		vb.setAlignment(Pos.CENTER);
		enteremail.setPrefWidth(50);
		label.setStyle(
				"  -fx-text-fill:#b19cd9;\n" + 
				"-fx-background-radius: 5;\n"+
				"-fx-font-size:25;\n"+
				"  -fx-pref-height:40px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:5px;\n" + 
				"  -fx-pref-width:300px; ");
		
		enteremail.setPromptText("Email");
		enterpass.setPromptText("Password");
		
		enteremail.setStyle(style);
		enterpass.setStyle(style);
		enter.setStyle(style1);
		register.setStyle(style1);
		
		register.setOnAction(e -> {
			App.setRegisterView();
		});
		
		enter.setOnAction(e -> {
			user data = new user();
			data.setEmail(enteremail.getText());
			data.setPassword(enterpass.getText());
			
			try {
				Gson g = new Gson();
				response res = MakeRequest.postRequest("/login", data);
				
				if(res != null) {
					if(res.getStatus().contentEquals("OK")) {
						File myObj = new File("auth.json");
						if(myObj.exists()) {
							user.WriteToken(data.getEmail(), res.getResult());
						}else {
							CreateTokenFile();
							user.WriteToken(data.getEmail(), res.getResult());
						}
						
						App.setMainView();
						
					}else {
						Alert a = new Alert(AlertType.ERROR);
						a.setHeaderText(res.getError());
						a.show();
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		});
		
		getChildren().addAll(vb);
		setMaxHeight(400);
		setMaxWidth(400);
	}
	
	
	public void CreateTokenFile() {
		 try {
		      File myObj = new File("auth.json");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}	
	
}
