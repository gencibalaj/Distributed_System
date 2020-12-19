import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import javafx.application.Platform;

public class ChatReciever extends Thread {
	Socket socket;
	InputStream input;
	BufferedReader reader;
	Runnable newMsg;
	private static MsgLabel newMsgCame;
	
	private static boolean chatrec = true;
	
	public ChatReciever() throws UnknownHostException, IOException {
		socket = ChatSender.socket;
		input = socket.getInputStream();
		reader = new BufferedReader(new InputStreamReader(input));
	}

	public static void setFlag(boolean flag) {
		chatrec = flag;
	}
	
	public void run(){
		newMsg = new Runnable() {
			
			@Override
			public void run() {
				ChatLayout.addMessagetoChat(newMsgCame);
			}
		}; 
		
		while(chatrec) {
			try {
				if(!socket.isClosed()) {
					byte[] a = new byte[100];
					input.read(a);
					Gson g = new Gson();
					String s = (new String(a,StandardCharsets.UTF_8)).replaceAll("\u0000.*", "");
					System.out.println(s.length());
					Message m = g.fromJson(s, Message.class);
					System.out.println(m.data);
					newMsgCame = new MsgLabel(m.getname().split("@")[0], m.getdata());
					Platform.runLater(newMsg);	
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		
	
	

	}
	
	
	
		

}
