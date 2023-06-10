package app.model;

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

}
