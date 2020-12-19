import java.util.Arrays;

public class Utils {
	
	
	public static byte[] add2BeginningOfArray(byte[] data, int i, int j){
	    byte[] newArray = Arrays.copyOf(data, data.length + 2);
	    newArray[0] = (byte)i;
	    newArray[1] = (byte)j;
	    System.arraycopy(data, 0, newArray, 2, data.length);
	    return newArray;
	}
}
