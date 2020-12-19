
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class MeetingLayout extends HBox{
	
	public MeetingLayout(ChatLayout c,CamerasLayout m) throws IOException {
		super(10);
		setPadding(new Insets(10));
		setStyle("-fx-border-width: 1px; -fx-border-color: green; ");
		m.prefWidthProperty().bind(this.widthProperty().divide(4).multiply(3));
		c.prefWidthProperty().bind(this.widthProperty().divide(4));
		
		m.prefHeightProperty().bind(this.heightProperty());
		c.prefHeightProperty().bind(this.heightProperty());
		getChildren().addAll(c,m);
		
	}
	
	

}
