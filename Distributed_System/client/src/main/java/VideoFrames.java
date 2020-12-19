
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VideoFrames extends Thread {
	ImageView iv ;
	String hostname = "3.16.21.183";
	int port = 7070;
	DatagramSocket socket;
	byte[] buffer = new byte[512];
	InetAddress address;
	public static Boolean sendFrames = true;
	Room room;
	static Boolean flag = false;
	
	
	public void setFlag(boolean flag) {
		if(!flag) {
			CamerasLayout.webcam.close();
		}
		this.sendFrames = flag;
	}
	
	public VideoFrames(ImageView iv, Room room) throws UnknownHostException, SocketException {
		this.iv = iv;
		this.room = room;
		address = InetAddress.getByName(hostname);
		System.out.println(address);
	    socket = new DatagramSocket();
	}
	public void run() {
		
		while( sendFrames) {
			//System.out.println("PO QOJ FRAMES");
			
			
			
			BufferedImage k = CamerasLayout.webcam.getImage();
			
			if(k == null) {
				return;
			}
			
			Image i = SwingFXUtils.toFXImage(k, null);
		    iv.setImage(i);
		    
			try {
				sendFrame(k);
				Thread.sleep(50);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
	}
	
	
	public void sendFrame(BufferedImage image) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", bos );
		byte [] data = bos.toByteArray();
		byte [] data1 = add2BeginningOfArray(data, room.nrp, 0);
		DatagramPacket request = new DatagramPacket(data1, data1.length,address , room.videoPort);
		socket.send(request);
	}
	
	
	public void leaveRoom() {
		byte[] leave = {(byte)room.nrp, 2};
		DatagramPacket request = new DatagramPacket(leave , leave.length, address, room.videoPort);
		try {
			socket.send(request);
			socket.close();
			sendFrames = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] add2BeginningOfArray(byte[] data, int i, int j)
	{
	    byte[] newArray = Arrays.copyOf(data, data.length + 2);
	    newArray[0] = (byte)i;
	    newArray[1] = (byte)j;
	    System.arraycopy(data, 0, newArray, 2, data.length);
	    return newArray;
	}
}



 

 

 

