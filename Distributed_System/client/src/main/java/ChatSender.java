
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class ChatSender {
	
	public static Socket socket;
	static Room room;
	private static MsgLabel newMsgCame;
	static String username;
	static String token;

	
	public ChatSender(Room r) throws IOException {
		this.room = r;
		System.out.println(r.chatport);
		socket = new Socket(InetAddress.getByName("3.16.21.183"), r.chatport);
		username =  App.getToken().getEmail();
		token = App.getToken().getToken();
		
		}
		
	
	
	
	
	public static void SendMsg(String msg) throws IOException {	
		
		
		
		DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
		
		try {
			newMsgCame = new MsgLabel(username,msg);
			newMsgCame.setmsgStyle();

			String json =  new GsonBuilder().create().toJson(new Message(msg,username, token));
			
			byte[] data  = json.getBytes();
			outToServer.write(data);
			
			ChatLayout.addMessagetoChat(newMsgCame);
			System.out.println("shkoj");
			
			
			//BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			//String s;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	}
	
	public static void SendCmd(String cmd) {	
		
		int id = room.nrp;
		try {
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
			String json =  new GsonBuilder().create().toJson(new Command(cmd, id, token, username));
			byte[] data  = json.getBytes();
			outToServer.write(data);
			if(cmd.contentEquals("leaveroom")) {
				//ChatReciever.setFlag(false);
				socket.close();
			}
			System.out.println("shkoj");
		} catch (IOException e) {
			e.printStackTrace();
			}
	}

}