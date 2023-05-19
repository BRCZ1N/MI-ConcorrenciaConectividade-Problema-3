package app.utilities;

import app.model.BankModel;

public enum BanksEnum {
	
	BANK_1(new BankModel("B0001","172.16.103.3:8000")),
	BANK_2(new BankModel("B0002","172.16.103.4:8000")),
	BANK_3(new BankModel("B0003","172.16.103.5:8000")),
	BANK_4(new BankModel("B0004","172.16.103.6:8000"));
	
	private BankModel bank;
	
	private BanksEnum(BankModel bank) {
		
		this.bank = bank;
		
	}

	public BankModel getBank() {
		
		return bank;
		
	}

	public void setBank(BankModel bank) {
		
		this.bank = bank;
		
	}
	
	

}
