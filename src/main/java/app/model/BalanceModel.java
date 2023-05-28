package app.model;

import app.utilities.OperationType;

public class BalanceModel extends OperationsModel{

	private AccountModel account;
	
	public BalanceModel(OperationType type, AccountModel account) {
		super(type);
		this.account = account;
	}

	public BalanceModel(AccountModel account) {

		super(OperationType.OP_BALANCE);
		this.account = account;

	}

	public AccountModel getAccount() {
		return account;
	}

	public void setAccount(AccountModel account) {
		this.account = account;
	}


}
