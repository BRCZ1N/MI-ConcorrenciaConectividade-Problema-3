package app.utilities;

/**
 * Enumeração que representa os tipos de operações disponíveis.
 */
public enum OperationType {

    OP_TRANSFER("Operacao de transferencia"),
    OP_DEPOSIT("Operacao de deposito"),
    OP_BALANCE("Operacao de saldo");

    private String typeOperation;

    /**
     * Construtor da enumeração OperationType.
     *
     * @param typeOperation O tipo de operação.
     */
    private OperationType(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    /**
     * Retorna o tipo de operação.
     *
     * @return O tipo de operação.
     */
    public String getTypeOperation() {
        return typeOperation;
    }

    /**
     * Define o tipo de operação.
     *
     * @param typeOperation O tipo de operação.
     */
    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

}
