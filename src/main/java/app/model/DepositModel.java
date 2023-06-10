package app.model;

import org.json.JSONObject;

import app.utilities.OperationType;

public class DepositModel extends OperationsModel {

	public DepositModel(OperationAccountModel accountOrigin, Double value) {

		super(accountOrigin, value, OperationType.OP_DEPOSIT);

	}

	public String toJSON() {

		JSONObject json = new JSONObject();

		json.put("accountOrigin", super.getAccountOrigin());
		json.put("value", super.getValue());
		json.put("type", super.getType());

		String jsonFormatMessage = json.toString();

		return jsonFormatMessage;

	}
}
