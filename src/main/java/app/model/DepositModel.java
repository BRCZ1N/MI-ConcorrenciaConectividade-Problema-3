package app.model;

import com.google.gson.Gson;

import app.utilities.OperationType;

/**
 * Classe responsável por representar a operação de deposito
 */
public class DepositModel extends OperationsModel {

	/**
	 * Construtor da classe DepositModel.
	 * 
	 * @param accountOrigin A conta de origem da operação.
	 * @param value         O valor do depósito.
	 */
	public DepositModel(OperationAccountModel accountOrigin, Double value) {

		super(accountOrigin, value, OperationType.OP_DEPOSIT);

	}

	/**
	 * Converte o objeto DepositModel para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto DepositModel.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);

	}
}
