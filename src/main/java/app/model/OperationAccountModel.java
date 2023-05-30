package app.model;

public class OperationAccountModel {

	private String idAccount;
	private BankModel bank;
	
	public OperationAccountModel() {
		
	}

	public OperationAccountModel(String idAccount, BankModel bank) {

		this.idAccount = idAccount;
		this.bank = bank;
	}

	public String getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}

	public BankModel getBank() {
		return bank;
	}

	public void setBank(BankModel bank) {
		this.bank = bank;
	}

}
