
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.bridj.cpp.com.OLEAutomationLibrary.UDATE;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VideoRecieve extends Thread {
	String hostname = "3.16.21.183";
	int port;
	DatagramSocket socket;
	byte[] buffer = new byte[512];
	InetAddress address ;
	Boolean sendFrames = true;
	byte[] receive = new byte[65535];
	DatagramPacket DpReceive = null; 
	DatagramSocket ds;
	Room room;
	ArrayList<Integer> ids = new ArrayList<Integer>();
	ArrayList<ImageView> images = new ArrayList<ImageView>();
	
	
	
	public VideoRecieve(Room room) throws UnknownHostException, SocketException {
		this.room = room;
		address = InetAddress.getByName(hostname);
		System.out.println(address);
		
	    socket = new DatagramSocket();
	    try {
			AddToRoom();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void AddToRoom() throws IOException {
		port = socket.getLocalPort();
		int Rid = room.nrp;
		byte[] addBytes = { (byte)Rid, 1 };
		//System.out.println(Rid + " " + room.videoPort);
		DatagramPacket request1 = new DatagramPacket(addBytes, addBytes.length, address ,room.videoPort);
		try {
			socket.send(request1);
			socket.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	private Runnable createRunnable(final ImageView image, final boolean type) {
		
		return new Runnable() {
	           public void run() {
	        	   if(type) {
	        		   CamerasLayout.addUser(image);
	        	   }else {
	        		   CamerasLayout.removeUser(image);
	        	   }
	           }
	    };
	}
	
	public void setFlag(boolean flag) {
		this.sendFrames = flag;
	}
	
	public void run() {
		
		try {
			ds = new DatagramSocket(port);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while( sendFrames) {
			DpReceive = new DatagramPacket(receive, receive.length); 
            try {           	
            	ds.receive(DpReceive);
            	AnalyseMessage();
			} catch (IOException e) {
				System.out.println(new String(receive));
				e.printStackTrace();
			}            
		}
	}
	
	
	
	int checkNewId(int id) {
		for(int i = 0; i < ids.size(); i++) {
			if( ids.get(i) == id) {
				return i;
			}
		}
		return -1;
	}
	
	
	
	public void AnalyseMessage() throws IOException {
		int id = (int)receive[0];
		int cmd = (int)receive[1];

		if(id == 0)
			return;
		
		if(cmd == 2) {
    		int index = checkNewId(id);
    		ImageView Img = images.get(index);
    		ids.remove(index);
    		images.remove(index);
    		Platform.runLater(createRunnable(Img, false));
			System.out.println("User Left: "+id);
    	}else if(cmd == 9) {
    		int index = checkNewId(id);
    		ImageView Img = images.get(index);
    		Image img = new Image("user.png"); 
    		Img.setImage(img);
    	}else if(cmd == 0){
			int index = checkNewId(id);
			if(index == -1){ 
				ImageView Img = new ImageView();
				images.add(Img);
				ids.add(id);
				index = images.size() -1;
				Platform.runLater(createRunnable(Img, true));
			}
			
			byte[] rec = Arrays.copyOfRange(receive, 2, receive.length);
			images.get(index).setVisible(true);
			images.get(index).setImage(toImage(rec));
		}
		
	}
	
	public Image toImage(byte[] data) throws IOException {
		//System.out.println(new String(data));
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		BufferedImage k = ImageIO.read(bis);
		Image i = SwingFXUtils.toFXImage(k, null);
		return i;	
	}
	
	
	public static StringBuilder data2(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    }
	
}



 

 

 

