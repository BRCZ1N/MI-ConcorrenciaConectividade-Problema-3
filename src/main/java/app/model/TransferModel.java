package app.model;

import com.google.gson.Gson;

import app.utilities.OperationType;

/**
 * Classe que representa um objeto de transferência de operações.
 */
public class TransferModel extends OperationsModel {

    private OperationAccountModel accountDestiny;

    /**
     * Construtor da classe TransferModel.
     *
     * @param accountOrigin   O objeto de conta de origem da transferência.
     * @param accountDestiny  O objeto de conta de destino da transferência.
     * @param value           O valor da transferência.
     */
    public TransferModel(OperationAccountModel accountOrigin, OperationAccountModel accountDestiny, Double value) {
        super(accountOrigin, value, OperationType.OP_TRANSFER);
        this.accountDestiny = accountDestiny;
    }

    /**
     * Retorna o objeto de conta de destino da transferência.
     *
     * @return O objeto de conta de destino da transferência.
     */
    public OperationAccountModel getAccountDestiny() {
        return accountDestiny;
    }

    /**
     * Define o objeto de conta de destino da transferência.
     *
     * @param accountDestiny O objeto de conta de destino da transferência.
     */
    public void setAccountDestiny(OperationAccountModel accountDestiny) {
        this.accountDestiny = accountDestiny;
    }

    /**
     * Converte o objeto TransferModel em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto TransferModel.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
