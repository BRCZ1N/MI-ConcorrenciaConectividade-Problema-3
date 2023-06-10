package app.model;
import org.json.JSONObject;

import app.utilities.OperationType;

public class TransferModel extends OperationsModel {

	private OperationAccountModel accountDestiny;

	public TransferModel(OperationAccountModel accountOrigin, Double value,OperationAccountModel accountDestiny) {
		super(accountOrigin, value, OperationType.OP_TRANSFER);
		this.accountDestiny = accountDestiny;
	}
	
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

		JSONObject json = new JSONObject();
		
		json.put("accountOrigin", super.getAccountOrigin());
		json.put("accountDestiny", this.accountDestiny);
		json.put("value", super.getValue());
		json.put("type", super.getType());

		String jsonFormatMessage = json.toString();

		return jsonFormatMessage;

	}

}
