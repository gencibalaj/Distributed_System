/*
import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

import javafx.scene.image.ImageView;

public class ScreenFrame extends Thread {
	ImageView iv ;
	String hostname = "3.16.21.183";
	int port = 7070;
	DatagramSocket socket;
	byte[] buffer = new byte[512];
	InetAddress address ;
	Boolean sendFrames = true;
	Boolean flag = false;
	Room room;
	
	public ScreenFrame(ImageView iv, Room room) throws UnknownHostException, SocketException {
		this.iv = iv;
		this.room = room;
		address = InetAddress.getByName(hostname);
		System.out.println(address);
	    socket = new DatagramSocket();
	    iv.setPreserveRatio(true);
		iv.setFitWidth(500);
	}
	
	public void setFlag() {
		this.flag = true;
	}
	
	public void run() {
		
		while( sendFrames) {
			
			if(flag) {
				String leave = "leaveroom "+room.nrp;
				System.out.println(leave);
				DatagramPacket request = new DatagramPacket(leave.getBytes(), leave.getBytes().length, address, room.videoPort);
				try {
					socket.send(request);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				java.awt.image.BufferedImage k = 
						new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				drawCursore(k);
				java.awt.image.BufferedImage kk = reSize(k, 1080, 720);
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				if(VideoApp.MYID != -1 ) {
					try {
						ImageIO.write(kk, "jpg", bos );
						byte [] data = bos.toByteArray();
						//System.out.println(data.length);
						//int[] a = {100 ,NrChunk(data, 10024)}; 
						//byte [] data1 = add2BeginningOfArray(data, a);
						byte[][] chunks = splitArray(data, 1024);
						for(int i = 0; i < chunks.length; i++) {
							DatagramPacket request = new DatagramPacket(chunks[i], chunks[i].length, address, room.videoPort);
							socket.send(request);
							Thread.sleep(1);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				javafx.scene.image.Image i = SwingFXUtils.toFXImage(k, null);
		        iv.setImage(i);
			} catch (HeadlessException e2) {
				e2.printStackTrace();
			} catch (AWTException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public byte[] AddToFrame(byte[] frame, byte[] chunk) {
		byte[] newFrame = new byte[frame.length+chunk.length];
		System.arraycopy(frame, 0, newFrame, 0, frame.length);
		System.arraycopy(chunk, 0, newFrame, frame.length, chunk.length);
		return newFrame;
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
	    return dimg;
	} 
	
	public void drawCursore(BufferedImage screenshot) {
		BufferedImage blackSquare = new BufferedImage(50, 50, BufferedImage.TYPE_3BYTE_BGR);
        for(int i = 0; i < blackSquare.getHeight(); i++){
            for(int j = 0; j < blackSquare.getWidth(); j++){
                blackSquare.setRGB(j, i, 128);
            }
        }   
            PointerInfo pointer = MouseInfo.getPointerInfo();
            int x = (int) pointer.getLocation().getX();
            int y = (int) pointer.getLocation().getY();
            screenshot.getGraphics().drawImage(blackSquare, x, y, null);
            
   }
	
	public BufferedImage reSize(BufferedImage originalImage,int IMG_WIDTH,int IMG_HEIGHT) {
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		return resizedImage;
	}
	
	public static byte[] add2BeginningOfArray(byte[] data, int[] a)
	{
	    byte[] newArray = Arrays.copyOf(data, data.length + a.length);
	    for(int i = 0; i < a.length; i++) {
	    	newArray[i] =(byte)a[i];
	    }
	    System.arraycopy(data, 0, newArray, a.length, data.length);
	    return newArray;
	}
	
	public static byte[] add2BeginningOfArray(byte[] data, int a)
	{
	    byte[] newArray = Arrays.copyOf(data, data.length + 1);
	    newArray[0] =(byte)a;
	    System.arraycopy(data, 0, newArray, 1, data.length);
	    return newArray;
	}
	
	public static byte[][] splitArray(byte[] arrayToSplit, int chunkSize){
	    int rest = arrayToSplit.length % chunkSize;
	    int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0);
	    byte[][] arrays = new byte[chunks][];
	    for(int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++){
	    	int[] a = {VideoApp.MYID, i == 0 ? 100 : 0}; 
	        arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
	        arrays[i] = add2BeginningOfArray(arrays[i], a);
	    }
	    if(rest > 0){
	    	int[] a = {VideoApp.MYID, 0}; 
	        arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
	        arrays[chunks - 1] = add2BeginningOfArray(arrays[chunks - 1], a);
	    }
	    return arrays;
	}
	
	public static int NrChunk(byte[] arrayToSplit, int chunkSize){
	    int rest = (arrayToSplit.length + 2) % chunkSize;
	    int chunks = (arrayToSplit.length + 2)/ chunkSize + (rest > 0 ? 1 : 0);
	    	//System.out.println(chunks);
	    return chunks;
	}
	
}*/



 

 

 

