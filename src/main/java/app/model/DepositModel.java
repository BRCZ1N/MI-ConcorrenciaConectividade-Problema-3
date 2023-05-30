package app.model;

import org.json.JSONObject;

import app.utilities.OperationType;

public class DepositModel extends OperationsModel {

	private OperationAccountModel account;
	private Double value;

	public DepositModel(OperationAccountModel account, Double value) {

		super(OperationType.OP_DEPOSIT);
		this.account = account;
		this.value = value;

	}

	public OperationAccountModel getAccount() {
		return account;
	}

	public void setAccount(OperationAccountModel account) {
		this.account = account;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String toJSON() {

		JSONObject json = new JSONObject();
		
		json.put("account", this.account);
		json.put("value", this.value);
		
		String jsonFormatMessage = json.toString();
		
		return jsonFormatMessage;

	}

}
