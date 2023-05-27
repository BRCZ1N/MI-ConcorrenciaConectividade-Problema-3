package app.model;

import app.utilities.OperationType;

public class TransferModel extends OperationsModel {

	private AccountModel accountOrigin;
	private AccountModel accountDestiny;

	public TransferModel(AccountModel accountOrigin, AccountModel accountDestiny, double value, String description) {

		super(value, OperationType.OP_TRANSFER, description);
		this.accountOrigin = accountOrigin;
		this.accountDestiny = accountDestiny;

	}

	public AccountModel getAccountOrigin() {
		return accountOrigin;
	}

	public void setAccountOrigin(AccountModel accountOrigin) {
		this.accountOrigin = accountOrigin;
	}

	public AccountModel getAccountDestiny() {
		return accountDestiny;
	}

	public void setAccountDestiny(AccountModel accountDestiny) {
		this.accountDestiny = accountDestiny;
	}

}
