package app.model;

import com.google.gson.Gson;

import app.utilities.OperationType;

/**
 * Classe que representa um modelo de operações.
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
     * @param type          O tipo da operação.
     */
    public OperationsModel(OperationAccountModel accountOrigin, Double value, OperationType type) {
        this.accountOrigin = accountOrigin;
        this.value = value;
        this.type = type;
    }

    /**
     * Retorna a conta de origem da operação.
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
     * Retorna o valor da operação.
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
     * Retorna o tipo da operação.
     *
     * @return O tipo da operação.
     */
    public OperationType getType() {
        return type;
    }

    /**
     * Define o tipo da operação.
     *
     * @param type O tipo da operação.
     */
    public void setType(OperationType type) {
        this.type = type;
    }

    /**
     * Converte o objeto OperationsModel em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto OperationsModel.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
