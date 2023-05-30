package app.model;

import org.json.JSONObject;

import app.utilities.OperationType;

public class TransferModel extends OperationsModel {

	private OperationAccountModel accountOrigin;
	private OperationAccountModel accountDestiny;
	private Double value;

	public TransferModel(OperationAccountModel accountOrigin, OperationAccountModel accountDestiny,Double value) {

		super(OperationType.OP_TRANSFER);
		this.accountOrigin = accountOrigin;
		this.accountDestiny = accountDestiny;
		this.value = value;

	}

	public OperationAccountModel getAccountOrigin() {
		return accountOrigin;
	}

	public void setAccountOrigin(OperationAccountModel accountOrigin) {
		this.accountOrigin = accountOrigin;
	}

	public OperationAccountModel getAccountDestiny() {
		return accountDestiny;
	}

	public void setAccountDestiny(OperationAccountModel accountDestiny) {
		this.accountDestiny = accountDestiny;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public String toJSON() {

		JSONObject json = new JSONObject();
		
		json.put("accountOrigin", this.accountOrigin);
		json.put("accountDestiny", this.accountDestiny);
		json.put("value", this.value);
		
		String jsonFormatMessage = json.toString();
		
		return jsonFormatMessage;

	}

}
