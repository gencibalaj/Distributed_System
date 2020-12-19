public class Room {
    	public int videoPort;
    	public int meetid;
    	public int nrp;
    	public int audioPort;
    	public int chatport;
    	@Override
    	public String toString() {
    		return "chatPort "+chatport+" Port:"+videoPort+" Id:"+meetid+" UserId:"+nrp;
    	}
}