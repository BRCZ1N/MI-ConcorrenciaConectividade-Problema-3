package app.model;

import org.json.JSONObject;

public class ReplySynchronObject {

	private long currentTimeStamp;

	public ReplySynchronObject(long currentTimeStamp) {

		this.currentTimeStamp = currentTimeStamp;

	}

	public long getCurrentTimeStamp() {
		return currentTimeStamp;
	}

	public void setCurrentTimeStamp(long currentTimeStamp) {
		this.currentTimeStamp = currentTimeStamp;
	}
	
	public String toJSON() {

		JSONObject json = new JSONObject();

		json.put("currentTimeStamp", this.currentTimeStamp);

		String jsonFormatMessage = json.toString();

		return jsonFormatMessage;

	}

}
