package app.model;

import com.google.gson.Gson;

import app.utilities.OperationType;

/**
 * Classe responsável por representar representar as operações de transferencia e deposito
 */
public class OperationsModel {

	private OperationAccountModel accountOrigin;
	private Double value;
	private OperationType type;

	/**
	 * Construtor da classe OperationsModel.
	 * 
	 * @param accountOrigin A conta de origem da operação.
	 * @param value         O valor da operação.
	 * @param type          O tipo de operação.
	 */
	public OperationsModel(OperationAccountModel accountOrigin, Double value, OperationType type) {

		this.accountOrigin = accountOrigin;
		this.value = value;
		this.type = type;

	}

	/**
	 * Obtém a conta de origem da operação.
	 * 
	 * @return A conta de origem da operação.
	 */
	public OperationAccountModel getAccountOrigin() {

		return accountOrigin;

	}

	/**
	 * Define a conta de origem da operação.
	 * 
	 * @param accountOrigin A conta de origem da operação.
	 */
	public void setAccountOrigin(OperationAccountModel accountOrigin) {

		this.accountOrigin = accountOrigin;

	}

	/**
	 * Obtém o valor da operação.
	 * 
	 * @return O valor da operação.
	 */
	public Double getValue() {

		return value;

	}

	/**
	 * Define o valor da operação.
	 * 
	 * @param value O valor da operação.
	 */
	public void setValue(Double value) {

		this.value = value;

	}

	/**
	 * Obtém o tipo de operação.
	 * 
	 * @return O tipo de operação.
	 */
	public OperationType getType() {

		return type;

	}

	/**
	 * Define o tipo de operação.
	 * 
	 * @param type O tipo de operação.
	 */
	public void setType(OperationType type) {

		this.type = type;

	}

	/**
	 * Converte o objeto OperationsModel para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto OperationsModel.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
