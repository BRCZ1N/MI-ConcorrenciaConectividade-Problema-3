package app.model;

import app.utilities.OperationType;

public class DepositModel extends OperationsModel {

	private AccountModel account;

	public DepositModel(AccountModel account, double value, String description) {

		super(value, OperationType.OP_DEPOSIT, description);
		this.account = account;

	}

	public AccountModel getAccount() {
		return account;
	}

	public void setAccount(AccountModel account) {
		this.account = account;
	}

}
