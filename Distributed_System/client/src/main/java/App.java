import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.google.gson.Gson;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class App extends Application {
	public static Stage primaryStage;
	public static boolean startthreads = false;
	//public static String username;
	public static Scene scene;
	
	private static user token;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
			this.primaryStage =  primaryStage;
			
			token = user.readToken();
			if(token == null || !authRequest(token)){
				setLoginView();
			}else {		
				setMainView();
			}
			App.primaryStage.setResizable(true);
			App.primaryStage.show();
	}
	
	public static user getToken() {
		return token;
	}
	
	public static void setToken(user token) {
		App.token = token;
	}
	
	public static void setRegisterView() {
		RegisterForm rf = new RegisterForm();
		Scene s = new Scene(rf);
		primaryStage.setScene(s);
	} 
	
	public static void setMainView() {
		SecondLayout sl = new SecondLayout();
		Scene s = new Scene(sl);
		primaryStage.setScene(s);
	}
	
	public static boolean authRequest(user data) {
		try {
			response result = MakeRequest.postRequest("/auth", data);
			if(result.getStatus().contentEquals("OK")) {
				System.out.println("TOKENI NE RREGULL. PO JU BON TFALA.");
				return true;
			}else {
				return false;
			}
		} catch (IOException | InterruptedException e) {
			Alert Error = new Alert(AlertType.ERROR);
			Error.setHeaderText("Problem in server.");
			Error.setContentText("Current server:" + MakeRequest.getServerUrl());
			Error.showAndWait();
			System.exit(0);
			return false;
		}
		
	}
	
	
	public static void setLoginView() {
		FirstScene rf = new FirstScene();
		Scene s = new Scene(rf);
		primaryStage.setScene(s);
	} 
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		 
		//ChatSender chs = new ChatSender();
		//chs.start();
		//ChatReciever chr= new ChatReciever();
		//chr.start();
		launch(args);
		  
			 
		 
	}
	
	
	
}
