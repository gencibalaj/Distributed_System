import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatLayout extends VBox {
	
	TextField tf = new TextField();
	//HBox hbox = new HBox(15);	
	static VBox msgs =  new VBox(10);
	private Room room;
	
	public ChatLayout(Room r) throws IOException, InterruptedException {
		this.room  = r;
	
		Thread.sleep(1000);
		
		
		final ChatSender chs = new ChatSender(room);
		
		ChatReciever chr= new ChatReciever();
		ChatReciever.setFlag(true);
		chr.start();
		
    	ScrollPane sp = new ScrollPane();
    	
    	sp.prefWidthProperty().bind(this.prefWidthProperty().subtract(20));
    	msgs.prefWidthProperty().bind(sp.prefWidthProperty());
    	
    	
    	
    	sp.prefHeightProperty().bind(this.prefHeightProperty());
    	msgs.prefHeightProperty().bind(sp.prefHeightProperty());
    	tf.prefWidthProperty().bind(this.prefWidthProperty());
    	
    	sp.setStyle("-fx-border-width: 0px");
    	
    	
    	getChildren().addAll(sp,tf);
    	sp.setHbarPolicy(ScrollBarPolicy.NEVER);
    	sp.setVbarPolicy(ScrollBarPolicy.NEVER);
    	msgs.setAlignment(Pos.TOP_CENTER);
    	
    	
    	//ScrollPane.positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, halignment, valignment, isSnapToPixel);
    	
 
    	//ScrollPane.positionInArea(msgs, 0, 0, sp.getWidth(), sp.getHeight(), 0, new Insets(0), HPos.CENTER, VPos.BASELINE, true);
    	sp.setContent(msgs);
    	
    	
    	sp.fitToHeightProperty();
    	sp.fitToWidthProperty();
    	sp.setFitToHeight(true);
    
    	//sp.setPrefSize(400, 850);
    	tf.setStyle("-fx-background-color:White;\n" + 
				"  -fx-text-fill:#9932CC;\n" + 
				"  -fx-pref-height:50px;\n" + 
				"-fx-font-weight: bold; -fx-border-width:1px; -fx-border-color:#9932CC;\n");
    	
    	
    

    
    	
    	
    	tf.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER && tf.getText().toString().replace(" ", "") != "") {
					try {
						ChatSender.SendMsg(tf.getText().toString());
						tf.setText("");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
				
			}
		});
    	
		setAlignment(Pos.BOTTOM_CENTER);
		
		
		
		
		
	}
	 public static void addMessagetoChat(MsgLabel m) {
		 	m.prefWidthProperty().bind(msgs.prefWidthProperty());
		 	msgs.getChildren().add(m);  
		 	
		    
	 }

	
	
	
}
