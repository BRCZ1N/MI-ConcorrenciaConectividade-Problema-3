package app.utilities;

public enum OperationType {

	OP_TRANSFER("Operacao de transferencia"),
	OP_DEPOSIT("Operacao de deposito"),
	OP_PAYMENT("Operacao de pagamento");

	private String typeOperation;

	private OperationType(String typeOperation) {

		this.typeOperation = typeOperation;

	}

	public String getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(String typeOperation) {
		this.typeOperation = typeOperation;
	}

}
