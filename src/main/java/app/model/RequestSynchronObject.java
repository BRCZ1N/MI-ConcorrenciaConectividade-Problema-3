package app.model;

import com.google.gson.Gson;

public class RequestSynchronObject{

	private long timeStamp;
	private OperationsModel operation;

	public RequestSynchronObject(long timeStamp, OperationsModel operation) {

		this.timeStamp = timeStamp;
		this.operation = operation;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public OperationsModel getOperation() {
		return operation;
	}

	public void setOperation(OperationsModel operation) {
		this.operation = operation;
	}
	
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}

}
