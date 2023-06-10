package app.model;

import app.utilities.OperationType;

public class OperationsModel {

	private OperationAccountModel accountOrigin;
	private long timeStamp;
	private Double value;
	private OperationType type;

	public OperationsModel(OperationAccountModel accountOrigin, long timeStamp, Double value, OperationType type) {

		this.accountOrigin = accountOrigin;
		this.timeStamp = timeStamp;
		this.value = value;
		this.type = type;
	}
	
	public OperationsModel(OperationAccountModel accountOrigin, Double value, OperationType type) {

		this.accountOrigin = accountOrigin;
		this.value = value;
		this.type = type;
	}

	public OperationAccountModel getAccountOrigin() {
		return accountOrigin;
	}

	public void setAccountOrigin(OperationAccountModel accountOrigin) {
		this.accountOrigin = accountOrigin;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public OperationType getType() {

		return type;

	}

	public void setType(OperationType type) {

		this.type = type;

	}

}
