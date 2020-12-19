import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ViewAttendance {

	
	public ViewAttendance(String html) {
		Stage s = new Stage(); 
		WebView browser = new WebView();
		browser.getEngine().loadContent(html, "text/html");
		s.setScene(new Scene(browser, 600, 600));
		s.show();
	}
}
