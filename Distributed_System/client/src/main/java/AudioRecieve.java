import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class AudioRecieve extends Thread {
	
	private final SourceDataLine sourceLine;
	DatagramSocket socket;
	Room room;
	int port;
	InetAddress address;
	DatagramPacket DpReceive = null; 
	DatagramSocket ds;
	AudioFormat format;
	byte[] receive = new byte[4024];
	private Boolean recAudio = true;
	
	public AudioRecieve(Room room) throws LineUnavailableException, IOException {
		this.room = room;
		address = InetAddress.getByName("3.16.21.183");
		format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 10000,8,1,1,10000,false);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);
		sourceLine = (SourceDataLine)AudioSystem.getLine(info);
		sourceLine.open();
		socket = new DatagramSocket();
		AddToRoom();
	}
	
	public void AddToRoom() throws IOException {
		port = socket.getLocalPort();
		int ROOMID = room.nrp;
		byte[] addBytes = { (byte)ROOMID, 1 };
		DatagramPacket request1 = new DatagramPacket(addBytes, addBytes.length, address ,room.audioPort);
		try {
			socket.send(request1);
			socket.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	public void setFlag(boolean flag) {
		this.recAudio = flag;
	}
	
	public void run() {
		sourceLine.start();
		try {
			ds = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		//ArrayList<AudioInputStream> FullFrame = new ArrayList<AudioInputStream>();
		//int j = 0;
		
		while(recAudio) {
			DpReceive = new DatagramPacket(receive, receive.length);
			try {
				ds.receive(DpReceive);
				if((int)receive[0] != 0) {
					sourceLine.write(receive, 0, receive.length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
