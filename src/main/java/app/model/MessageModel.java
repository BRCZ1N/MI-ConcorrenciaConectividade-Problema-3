package app.model;

import org.json.JSONObject;

public class MessageModel {

	private String message;

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

		JSONObject json = new JSONObject(this.message);
		String jsonFormatMessage = json.toString();

		return jsonFormatMessage;

	}

}
