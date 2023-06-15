package app.utilities;

/**
 * Enumeração que representa os tipos de operação disponíveis.
 */
public enum OperationType {

	OP_TRANSFER("Operacao de transferencia"),
	OP_DEPOSIT("Operacao de deposito"),
	OP_BALANCE("Operacao de saldo");

	private String typeOperation;

	/**
	 * Cria uma instância de OperationType com o tipo de operação especificado.
	 * 
	 * @param typeOperation O tipo de operação.
	 */
	private OperationType(String typeOperation) {

		this.typeOperation = typeOperation;

	}

	/**
	 * Obtém o tipo de operação.
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
