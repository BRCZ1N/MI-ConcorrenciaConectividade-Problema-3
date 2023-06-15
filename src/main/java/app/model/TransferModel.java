package app.model;
import com.google.gson.Gson;

import app.utilities.OperationType;

/**
 * Classe responsável por representar a operação de transferencia do banco
 */
public class TransferModel extends OperationsModel {

	private OperationAccountModel accountDestiny;

	/**
	 * Construtor da classe TransferModel.
	 * 
	 * @param accountOrigin  A conta de origem da transferência.
	 * @param accountDestiny A conta de destino da transferência.
	 * @param value          O valor da transferência.
	 */
	public TransferModel(OperationAccountModel accountOrigin, OperationAccountModel accountDestiny, Double value) {
		super(accountOrigin, value, OperationType.OP_TRANSFER);
		this.accountDestiny = accountDestiny;
	}

	/**
	 * Obtém a conta de destino da transferência.
	 * 
	 * @return A conta de destino da transferência.
	 */
	public OperationAccountModel getAccountDestiny() {
		return accountDestiny;
	}

	/**
	 * Define a conta de destino da transferência.
	 * 
	 * @param accountDestiny A conta de destino da transferência.
	 */
	public void setAccountDestiny(OperationAccountModel accountDestiny) {
		this.accountDestiny = accountDestiny;
	}
	
	/**
	 * Converte o objeto TransferModel para uma representação JSON.
	 * 
	 * @return A representação JSON do objeto TransferModel.
	 */
	public String toJSON() {

		Gson gson = new Gson();
		return gson.toJson(this);
		
	}

}
