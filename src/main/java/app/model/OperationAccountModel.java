package app.model;

import com.google.gson.Gson;

/**
 * Classe responsável para auxiliar as operações do banco com o id da conta e o banco da conta
 */
public class OperationAccountModel {

	private String idAccount;
	private BankModel bank;

	/**
	 * Construtor padrão da classe OperationAccountModel.
	 */
	public OperationAccountModel() {

	}

	/**
	 * Construtor da classe OperationAccountModel.
	 * 
	 * @param idAccount O ID da conta de operação.
	 * @param bank      O banco associado à conta de operação.
	 */
	public OperationAccountModel(String idAccount, BankModel bank) {

		this.idAccount = idAccount;
		this.bank = bank;
	}

	/**
	 * Obtém o ID da conta de operação.
	 * 
	 * @return O ID da conta de operação.
	 */
	public String getIdAccount() {
		return idAccount;
	}

	/**
	 * Define o ID da conta de operação.
	 * 
	 * @param idAccount O ID da conta de operação.
	 */
	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}

	/**
	 * Obtém o banco associado à conta de operação.
	 * 
	 * @return O banco associado à conta de operação.
	 */
	public BankModel getBank() {
		return bank;
	}

	/**
	 * Define o banco associado à conta de operação.
	 * 
	 * @param bank O banco associado à conta de operação.
	 */
	public void setBank(BankModel bank) {
		this.bank = bank;
	}

	/**
	 * Converte o objeto OperationAccountModel para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto OperationAccountModel.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
