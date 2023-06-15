package app.model;

import com.google.gson.Gson;

/**
 * Classe responsável por guardar os dados principais para identificação do banco
 */
public class BankModel {

	private Long id;
	private String ip;

	/**
	 * Construtor padrão da classe BankModel.
	 */
	public BankModel() {

	}

	/**
	 * Construtor da classe BankModel.
	 * 
	 * @param id O ID do banco.
	 * @param ip O endereço IP do banco.
	 */
	public BankModel(Long id, String ip) {

		this.id = id;
		this.ip = ip;

	}
	
	/**
	 * Obtém o ID do banco.
	 * 
	 * @return O ID do banco.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Define o ID do banco.
	 * 
	 * @param id O ID do banco.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém o endereço IP do banco.
	 * 
	 * @return O endereço IP do banco.
	 */
	public String getIp() {

		return ip;

	}

	/**
	 * Define o endereço IP do banco.
	 * 
	 * @param ip O endereço IP do banco.
	 */
	public void setIp(String ip) {

		this.ip = ip;

	}

	/**
	 * Converte o objeto BankModel para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto BankModel.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}


}
