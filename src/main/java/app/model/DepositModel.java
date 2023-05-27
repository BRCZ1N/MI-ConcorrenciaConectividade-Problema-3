package app.model;

import app.utilities.OperationType;

public class DepositModel extends OperationsModel {

	private AccountModel account;
	private Double value;

	public DepositModel(AccountModel account, Double value) {

		super(OperationType.OP_DEPOSIT);
		this.account = account;

	}

	public AccountModel getAccount() {
		return account;
	}

	public void setAccount(AccountModel account) {
		this.account = account;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
