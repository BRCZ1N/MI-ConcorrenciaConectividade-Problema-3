package app.model;

import java.util.ArrayList;
import org.json.JSONObject;

public class AccountModel {

	private String id;
	private ArrayList<UserModel> beneficiares;
	private String password;
	private Double balance;
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

	public void setBalance(Double balance) {

		this.balance = balance;

	}

	public BankModel getBank() {

		return bank;

	}

	public void setBank(BankModel bank) {

		this.bank = bank;

	}

	public String toJSON() {

		JSONObject json = new JSONObject();

		json.put("password", this.password);
		json.put("bank", this.bank);
		json.put("beneficiares", this.beneficiares);
		String jsonFormatMessage = json.toString();

		return jsonFormatMessage;

	}

}
