
public class response {

	private String status;
	private String error;
	private String result;
	
	
	public response(String status, String error) {
		this.status = status;
		this.error = error; 
	}
	
	public response(String status, String error, String result) {
		this.status = status;
		this.error = error; 
		this.setResult(result); 
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
