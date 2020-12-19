import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Message{
	
	public String data;
	public String name;
	public String token;
	
	
	public Message(String content,String from, String token) {
		this.data = content;
		this.name  = from;
		this.token = token;
	}
	
	@Override
	public String toString() {
		return "data:"+data+" name:"+name;
	}
	
	public String getdata() {
		return this.data;
	}
	public String getname() {
		return this.name;
	}
}
	

