package app.model;

import app.utilities.OperationType;

public class OperationsModel {

	private Double value;
	private OperationType type;
	private String description;

	public OperationsModel(Double value, OperationType type, String description) {
		
		this.value = value;
		this.type = type;
		this.description = description;
		
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

	public String getDescription() {
		
		return description;
		
	}

	public void setDescription(String description) {
		
		this.description = description;
		
	}

}
