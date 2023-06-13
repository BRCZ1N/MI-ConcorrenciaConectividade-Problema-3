package app.model;

import com.google.gson.Gson;

import app.utilities.OperationType;

/**
 * Classe que representa um modelo de depósito.
 */
public class DepositModel extends OperationsModel {

    /**
     * Construtor da classe DepositModel.
     *
     * @param accountOrigin A conta de origem do depósito.
     * @param value         O valor do depósito.
     */
    public DepositModel(OperationAccountModel accountOrigin, Double value) {
        super(accountOrigin, value, OperationType.OP_DEPOSIT);
    }

    /**
     * Converte o objeto DepositModel em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto DepositModel.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
