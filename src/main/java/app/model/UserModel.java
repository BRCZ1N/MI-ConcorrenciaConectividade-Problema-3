package app.model;

import com.google.gson.Gson;

public class UserModel {

	private String name;
	private String cpf;

	public UserModel(String name, String cpf) {

		this.name = name;
		this.cpf = cpf;

	}

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public String getCpf() {

		return cpf;

	}

	public void setCpf(String cpf) {

		this.cpf = cpf;

	}

	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}

}
