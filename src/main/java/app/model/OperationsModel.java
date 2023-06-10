package app.model;

import app.utilities.OperationType;

public class OperationsModel {

	private OperationAccountModel accountOrigin;
	private Double value;
	private OperationType type;

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

	public OperationType getType() {

		return type;

	}

	public void setType(OperationType type) {

		this.type = type;

	}

}
