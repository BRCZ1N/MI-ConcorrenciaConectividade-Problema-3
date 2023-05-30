package app.model;

import org.json.JSONObject;

public class LoginAccountModel {

	private String id;
	private String password;

	public LoginAccountModel(String id, String password) {

		this.id = id;
		this.password = password;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toJSON() {

		JSONObject json = new JSONObject();
		
		json.put("accountOrigin", this.id);
		json.put("accountDestiny", this.password);
		
		String jsonFormatMessage = json.toString();
		
		return jsonFormatMessage;

	}
	
}
