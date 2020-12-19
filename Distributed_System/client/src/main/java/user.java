import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class user {
	
	
	private String name;
	private String email;
	private String password;
	private String token;
	
	
	public user(String name, String email, String password) {
		this.name = name;
		this.setEmail(email);
		this.setPassword(password);
	}
	
	public user(){
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public static user readToken() {
		try {
	      File myObj = new File("auth.json");
	      Scanner myReader = new Scanner(myObj);
	      String data = "";
	      while (myReader.hasNextLine()) {
	        data += myReader.nextLine();
	      }
	      myReader.close();
	      Gson g = new Gson();
	      user u = g.fromJson(data, user.class);
	      if (u.getEmail() == null || u.getToken() == null) return null; 
	      return u;
	    } catch (FileNotFoundException e) {
	     	return null;
	    }
	}
	
	public static void WriteToken(String email, String token) {
		try { 
				user u = new user();
				u.setEmail(email);
				u.setToken(token);
				Gson g = new Gson();
				App.setToken(u);
				System.out.println(g.toJson(u));
				String data = g.toJson(u);
				
				FileWriter myWriter = new FileWriter("auth.json");
				myWriter.write(data);
				myWriter.close();
				System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		    	System.out.println("An error occurred.");
		    	e.printStackTrace();
		    }
	}
}
	
