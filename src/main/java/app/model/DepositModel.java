package app.model;

import com.google.gson.Gson;

import app.utilities.OperationType;

public class DepositModel extends OperationsModel {

	public DepositModel(OperationAccountModel accountOrigin, Double value) {

		super(accountOrigin, value, OperationType.OP_DEPOSIT);

	}

	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}
}
