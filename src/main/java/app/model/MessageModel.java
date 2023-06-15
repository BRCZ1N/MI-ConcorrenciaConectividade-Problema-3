package app.model;

import com.google.gson.Gson;

/**
 * Classe responsável por representar mensagens de retorno
 */
public class MessageModel {

	private String message;

	/**
	 * Construtor padrão da classe MessageModel.
	 */
	public MessageModel() {

	}

	/**
	 * Construtor da classe MessageModel.
	 * 
	 * @param message A mensagem.
	 */
	public MessageModel(String message) {

		this.message = message;

	}

	/**
	 * Obtém a mensagem.
	 * 
	 * @return A mensagem.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Define a mensagem.
	 * 
	 * @param message A mensagem.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Converte o objeto MessageModel para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto MessageModel.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}
	
	/**
	 * Retorna uma representação em formato de string do objeto MessageModel.
	 * 
	 * @return A representação em formato de string do objeto MessageModel.
	 */
	@Override
	public String toString() {
		
		return this.message;		
	
	}

}
