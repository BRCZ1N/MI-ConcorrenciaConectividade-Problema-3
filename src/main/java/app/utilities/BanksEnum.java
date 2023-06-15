package app.utilities;

import app.model.BankModel;

/**
 * Enumeração que representa os bancos disponíveis.
 */
public enum BanksEnum {
	
	BANK_1(new BankModel(1L,"http://172.16.103.3:8000")),
	BANK_2(new BankModel(2L,"http://172.16.103.4:8000")),
	BANK_3(new BankModel(3L,"http://172.16.103.5:8000")),
	BANK_4(new BankModel(4L,"http://172.16.103.6:8000"));
	
	private BankModel bank;
	
	/**
	 * Cria uma instância de BanksEnum com o banco especificado.
	 * 
	 * @param bank O modelo do banco.
	 */
	private BanksEnum(BankModel bank) {
		
		this.bank = bank;
		
	}

	/**
	 * Obtém o modelo do banco.
	 * 
	 * @return O modelo do banco.
	 */
	public BankModel getBank() {
		
		return bank;
		
	}

	/**
	 * Define o modelo do banco.
	 * 
	 * @param bank O modelo do banco.
	 */
	public void setBank(BankModel bank) {
		
		this.bank = bank;
		
	}
	
	

}
