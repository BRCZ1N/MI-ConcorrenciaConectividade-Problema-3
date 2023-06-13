package app.model;

import com.google.gson.Gson;

public class MessageModel {

	private String message;

	public MessageModel() {

	}

	public MessageModel(String message) {

		this.message = message;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}
	
	@Override
	
	public String toString() {
		
		return this.message;		
	
	}

}
