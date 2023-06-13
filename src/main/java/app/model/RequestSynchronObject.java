package app.model;

import org.json.JSONObject;

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

		JSONObject json = new JSONObject();

		json.put("timeStamp", this.timeStamp);
		json.put("operation", this.operation.toString());

		String jsonFormatMessage = json.toString();

		return jsonFormatMessage;

	}

}
