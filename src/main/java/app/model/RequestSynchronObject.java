package app.model;

import com.google.gson.Gson;

/**
 * Classe responsável por representar a requisição que um banco faz ao outro com o seu timeStamp e a operação com a região crítica do banco
 */
public class RequestSynchronObject{

	private long timeStamp;
	private OperationsModel operation;

	/**
	 * Construtor da classe RequestSynchronObject.
	 * 
	 * @param timeStamp O timestamp da solicitação.
	 * @param operation O objeto OperationsModel contendo a operação.
	 */
	public RequestSynchronObject(long timeStamp, OperationsModel operation) {

		this.timeStamp = timeStamp;
		this.operation = operation;
	}

	/**
	 * Obtém o timestamp da solicitação.
	 * 
	 * @return O timestamp da solicitação.
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Define o timestamp da solicitação.
	 * 
	 * @param timeStamp O timestamp da solicitação.
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Obtém o objeto OperationsModel contendo a operação.
	 * 
	 * @return O objeto OperationsModel contendo a operação.
	 */
	public OperationsModel getOperation() {
		return operation;
	}

	/**
	 * Define o objeto OperationsModel contendo a operação.
	 * 
	 * @param operation O objeto OperationsModel contendo a operação.
	 */
	public void setOperation(OperationsModel operation) {
		this.operation = operation;
	}
	
	/**
	 * Converte o objeto RequestSynchronObject para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto RequestSynchronObject.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}

}
