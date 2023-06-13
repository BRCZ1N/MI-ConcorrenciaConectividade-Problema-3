package app.model;

import com.google.gson.Gson;

public class BankModel {

	private Long id;
	private String ip;

	public BankModel() {

	}

	public BankModel(Long id, String ip) {

		this.id = id;
		this.ip = ip;

	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
