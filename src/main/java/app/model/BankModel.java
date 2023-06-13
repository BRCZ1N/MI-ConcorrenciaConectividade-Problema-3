package app.model;

import com.google.gson.Gson;

public class BankModel {

	private String id;
	private String ip;

	public BankModel(String id, String ip) {

		this.id = id;
		this.ip = ip;

	}

	public BankModel() {

	}

	public String getId() {

		return id;

	}

	public void setId(String id) {

		this.id = id;

	}

	public String getIp() {

		return ip;

	}

	public void setIp(String ip) {

		this.ip = ip;

	}

	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}


}
