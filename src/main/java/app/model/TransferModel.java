package app.model;

import app.utilities.OperationType;

public class TransferModel extends OperationsModel {

	private AccountModel accountOrigin;
	private AccountModel accountDestiny;
	private Double value;

	public TransferModel(AccountModel accountOrigin, AccountModel accountDestiny, Double value) {

		super(OperationType.OP_TRANSFER);
		this.accountOrigin = accountOrigin;
		this.accountDestiny = accountDestiny;
		this.value = value;

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

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
