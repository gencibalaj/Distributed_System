import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;

public class AudioSender extends Thread {
	
	private final TargetDataLine targetLine;
	
	private Boolean sendAudio = true;
	private Room room;
	private String ip = "3.16.21.183";
	InetAddress address;
	DatagramSocket socket;
	
	public AudioSender(Room room) throws LineUnavailableException, UnknownHostException, SocketException {
		this.room = room;
		address = InetAddress.getByName(ip);
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 10000,8,1,1,10000,false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
		targetLine = (TargetDataLine)AudioSystem.getLine(info);
		targetLine.open();
		socket = new DatagramSocket();
	}
	
	public void setFlag(boolean flag) {
		this.sendAudio = flag;
	}
	
	public void run() {
		targetLine.start();
		while(sendAudio) {
			byte[] data = new byte[4024];
			int bytes = targetLine.read(data, 0, data.length);
			
			try {
				sendAudio(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendAudio(byte[] data) throws IOException {
		byte [] data1 = Utils.add2BeginningOfArray(data, room.nrp, 0);
		DatagramPacket request = new DatagramPacket(data1, data1.length,address , room.audioPort);
		socket.send(request);
	}
	
}
