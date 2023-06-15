package app.model;

import com.google.gson.Gson;

/**
 * Classe responsável por representar a resposta do servidor para outro servidor solicitante com o timestamp do proprio servidor
 */
public class ReplySynchronObject {

	private long currentTimeStamp;

	/**
	 * Construtor da classe ReplySynchronObject.
	 * 
	 * @param currentTimeStamp O timestamp atual.
	 */
	public ReplySynchronObject(long currentTimeStamp) {

		this.currentTimeStamp = currentTimeStamp;

	}

	/**
	 * Obtém o timestamp atual.
	 * 
	 * @return O timestamp atual.
	 */
	public long getCurrentTimeStamp() {
		return currentTimeStamp;
	}

	/**
	 * Define o timestamp atual.
	 * 
	 * @param currentTimeStamp O timestamp atual.
	 */
	public void setCurrentTimeStamp(long currentTimeStamp) {
		this.currentTimeStamp = currentTimeStamp;
	}
	
	/**
	 * Converte o objeto ReplySynchronObject para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto ReplySynchronObject.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);
		
	}

}
