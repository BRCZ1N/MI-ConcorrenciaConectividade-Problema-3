package app.model;

import java.util.ArrayList;

import com.google.gson.Gson;

/**
 * Classe responsável por representar uma conta do banco
 */
public class AccountModel {

	private String id;
	private ArrayList<UserModel> beneficiares;
	private String password;
	private double balance = 0;
	private BankModel bank;

	/**
	 * Construtor padrão da classe AccountModel.
	 */
	public AccountModel() {

	}

	/**
	 * Construtor da classe AccountModel.
	 * 
	 * @param password     A senha da conta.
	 * @param bank         O banco associado à conta.
	 * @param beneficiares Os beneficiários da conta.
	 */
	public AccountModel(String password, BankModel bank, ArrayList<UserModel> beneficiares) {

		this.password = password;
		this.bank = bank;
		this.beneficiares = beneficiares;

	}

	/**
	 * Obtém o ID da conta.
	 * 
	 * @return O ID da conta.
	 */
	public String getId() {

		return id;

	}

	/**
	 * Define o ID da conta.
	 * 
	 * @param id O ID da conta.
	 */
	public void setId(String id) {

		this.id = id;

	}

	/**
	 * Obtém a lista de beneficiários da conta.
	 * 
	 * @return A lista de beneficiários da conta.
	 */
	public ArrayList<UserModel> getBeneficiares() {

		return beneficiares;

	}

	/**
	 * Define a lista de beneficiários da conta.
	 * 
	 * @param beneficiares A lista de beneficiários da conta.
	 */
	public void setBeneficiares(ArrayList<UserModel> beneficiares) {

		this.beneficiares = beneficiares;

	}

	/**
	 * Obtém a senha da conta.
	 * 
	 * @return A senha da conta.
	 */
	public String getPassword() {

		return password;

	}

	/**
	 * Define a senha da conta.
	 * 
	 * @param password A senha da conta.
	 */
	public void setPassword(String password) {

		this.password = password;

	}
	
	/**
	 * Obtém o saldo da conta.
	 * 
	 * @return O saldo da conta.
	 */
	public Double getBalance() {

		return balance;

	}

	/**
	 * Define o saldo da conta.
	 * 
	 * @param balance O saldo da conta.
	 */
	public void setBalance(double balance) {

		this.balance = balance;

	}

	/**
	 * Obtém o banco associado à conta.
	 * 
	 * @return O banco associado à conta.
	 */
	public BankModel getBank() {

		return bank;

	}

	/**
	 * Define o banco associado à conta.
	 * 
	 * @param bank O banco associado à conta.
	 */
	public void setBank(BankModel bank) {

		this.bank = bank;

	}

	/**
	 * Converte o objeto AccountModel para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto AccountModel.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}

}
