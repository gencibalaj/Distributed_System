import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MsgLabel extends Label {
	public String data;
	public String name;
	public MsgLabel(String n,String d){
		
		
	
		
		this.name = n;
		this.data = d;
		setText("  "+this.name+": "+this.data);
		setStyle("-fx-background-color:#b19cd9;\n" + 
				"  -fx-text-fill:white;\n" + 
				"-fx-background-radius: 5;\n"+
				"  -fx-pref-height:50px;\n" +
				"-fx-font-weight: bold;\n" +
				"  -fx-padding:15px;");
		
	}
	public void setmsgStyle() {
		setStyle("-fx-background-color:#9932CC;\n" + 
				"-fx-background-radius: 5;\n"+
				"-fx-font-weight: bold;\n" +
				"  -fx-text-fill:white;\n" + 
				"  -fx-text-align: right;\n" + 
				"  -fx-padding:15px;\n" + 
				"  -fx-pref-height:50px;");
		setAlignment(Pos.BASELINE_RIGHT);
	
	}
}
