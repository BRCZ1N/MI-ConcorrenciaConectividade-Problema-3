package app.model;

import com.google.gson.Gson;

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

		Gson gson = new Gson();
		return gson.toJson(this);
		
	}

}
