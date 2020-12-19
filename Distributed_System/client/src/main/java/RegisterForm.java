import java.io.IOException;

import com.google.gson.Gson;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RegisterForm extends VBox {
	
	private Label label = new Label("Register");
	private TextField entername =  new TextField();
	private TextField enteremail =  new TextField();
	private PasswordField enterpass =  new PasswordField();
	private Button register = new Button("REGISTER");
	private Button login = new Button("<");
	private HBox hb = new HBox(20);
	
	public RegisterForm() {
		super(40);
		setPadding(new Insets(50));
		label.setStyle(
				"  -fx-text-fill:#b19cd9;\n" + 
				"-fx-background-radius: 5;\n"+
				"-fx-font-size:25;\n"+
				"  -fx-pref-height:40px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:5px;\n" + 
				"  -fx-pref-width:300px; ");
		
		hb.getChildren().addAll(register, login);
		
		hb.setAlignment(Pos.CENTER);
		
		
		getChildren().addAll(label, entername, enteremail,enterpass , hb);
		
		
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
		
		
		entername.setStyle(style);
		enteremail.setStyle(style);
		enterpass.setStyle(style);
		register.setStyle(style1);
		login.setStyle(style1);
		
		setAlignment(Pos.CENTER);
		
		enteremail.setPromptText("Email");
		entername.setPromptText("Name");
		enterpass.setPromptText("Password");
		
		login.setOnAction(e -> {
			App.setLoginView();
		});
		
		register.setOnAction(e -> {
			
			user data = new user();
			data.setEmail(enteremail.getText());
			data.setName(entername.getText());
			data.setPassword(enterpass.getText());
			
			try {
				response res = MakeRequest.postRequest("/register", data);
				
				if(res != null) {
					if(res.getStatus().contentEquals("OK")) {
						Alert a = new Alert(AlertType.CONFIRMATION);
						a.setHeaderText("Account Created");
						a.showAndWait();
						App.setLoginView();
					}else {
						Alert a = new Alert(AlertType.ERROR);
						a.setHeaderText(res.getError());
						a.show();
					}
				}
				//System.out.println(result);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		});
		
		
	}
}
