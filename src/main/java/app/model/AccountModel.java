package app.model;

import java.util.ArrayList;

import com.google.gson.Gson;

public class AccountModel {

	private String id;
	private ArrayList<UserModel> beneficiares;
	private String password;
	private double balance = 0;
	private BankModel bank;

	public AccountModel() {

	}

	public AccountModel(String password, BankModel bank, ArrayList<UserModel> beneficiares) {

		this.password = password;
		this.bank = bank;
		this.beneficiares = beneficiares;

	}

	public String getId() {

		return id;

	}

	public void setId(String id) {

		this.id = id;

	}

	public ArrayList<UserModel> getBeneficiares() {

		return beneficiares;

	}

	public void setBeneficiares(ArrayList<UserModel> beneficiares) {

		this.beneficiares = beneficiares;

	}

	public String getPassword() {

		return password;

	}

	public void setPassword(String password) {

		this.password = password;

	}

	public Double getBalance() {

		return balance;

	}

	public void setBalance(double balance) {

		this.balance = balance;

	}

	public BankModel getBank() {

		return bank;

	}

	public void setBank(BankModel bank) {

		this.bank = bank;

	}

	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}

}
