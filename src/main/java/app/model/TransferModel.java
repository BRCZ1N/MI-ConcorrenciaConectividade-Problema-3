package app.model;
import com.google.gson.Gson;

import app.utilities.OperationType;

public class TransferModel extends OperationsModel {

	private OperationAccountModel accountDestiny;

	public TransferModel(OperationAccountModel accountOrigin, OperationAccountModel accountDestiny, Double value) {
		super(accountOrigin, value, OperationType.OP_TRANSFER);
		this.accountDestiny = accountDestiny;
	}

	public OperationAccountModel getAccountDestiny() {
		return accountDestiny;
	}

	public void setAccountDestiny(OperationAccountModel accountDestiny) {
		this.accountDestiny = accountDestiny;
	}
	
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);
		
	}

}
