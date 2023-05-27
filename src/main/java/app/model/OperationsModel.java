package app.model;

import app.utilities.OperationType;

public class OperationsModel {

	private OperationType type;

	public OperationsModel(OperationType type) {
		
		this.type = type;
		
	}

	public OperationType getType() {
		
		return type;
		
	}

	public void setType(OperationType type) {
		
		this.type = type;
		
	}

}
