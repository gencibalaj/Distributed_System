/*
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

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ScreenRecieve extends Thread {
	String hostname = "3.16.21.183";
	int port;
	DatagramSocket socket;
	byte[] buffer = new byte[512];
	InetAddress address ;
	Boolean sendFrames = true;
	byte[] receive = new byte[1026];
	DatagramPacket DpReceive = null; 
	DatagramSocket ds;
	Room room;
	ArrayList<Integer> ids = new ArrayList<Integer>();
	ArrayList<ImageView> images = new ArrayList<ImageView>();
	
	
	
	public ScreenRecieve(Room room) throws UnknownHostException, SocketException {
		this.room = room;
		address = InetAddress.getByName(hostname);
		System.out.println(address);
		
	    socket = new DatagramSocket();
	    try {
			AddToRoom();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    populateGrid();
	}
	
	public void populateGrid() {
		for(int i = 0; i < 20; i++) {
			images.add(new ImageView());
			VideoApp.root.getChildren().add(images.get(i));
		}
	}
	
	
	
	public void AddToRoom() throws IOException {
		port = socket.getLocalPort();
		System.out.println("ELTIONI");
		VideoApp.MYID = room.nrp;
		System.out.println(room.toString());
		String add = "addroom "+room.nrp;
		DatagramPacket request1 = new DatagramPacket(add.getBytes(), add.getBytes().length,address ,room.videoPort);
		try {
			System.out.println( socket.getLocalPort());
			socket.send(request1);
			socket.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	
	
	public void run() {
		try {
			ds = new DatagramSocket(port);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			byte[] frame = new byte[0];
			int chunksize = 0;
			Boolean firstframe = false;
			int n = 0;
			while( sendFrames) {
				DpReceive = new DatagramPacket(receive, receive.length); 
	                
	                try {
						ds.receive(DpReceive);
						//System.out.println(n+". First: "+(int)receive[0] + " Second: "+(int)receive[1]);	
		            	n++;
	            	if((int)receive[0] != 0 && (int)receive[1] == 100) {
	            		if(frame.length != 0 && firstframe) {
	            			System.out.println("NEW FRAME");
	            			images.get(0).setImage(toImage(frame));
	            			frame = new byte[0];
	            		}
	            		
	            		if(!firstframe) {
	            			firstframe = true;
	            			frame = new byte[0];
	            			System.out.println("Drop this Frame");
	            		}
	            		
	            		byte[] f = Arrays.copyOfRange(receive, 2, receive.length);
	            		frame = AddToFrame(frame, f);
	            	
	            	}else if((int)receive[0] != 0 ){
	            		byte[] f = Arrays.copyOfRange(receive, 2, receive.length);
	            		//System.out.println(f.length);
	            		frame = AddToFrame(frame, f);
	            	}
	            	}catch (IOException e) {
						// TODO Auto-generated catch block
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
	
	public byte[] AddToFrame(byte[] frame, byte[] chunk) {
		byte[] newFrame = new byte[frame.length+chunk.length];
		System.arraycopy(frame, 0, newFrame, 0, frame.length);
		System.arraycopy(chunk, 0, newFrame, frame.length, chunk.length);
		return newFrame;
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

*/

 

 

 

