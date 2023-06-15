package app.exceptions;

/**
 * Classe da exceção lançada quando ocorre uma tentativa de realizar uma operação com saldo insuficiente.
 */
public class InsufficientBalanceException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Cria uma nova instância de InsufficientBalanceException com a mensagem padrão "Saldo insuficiente".
	 */
	public InsufficientBalanceException() {
		
		super("Saldo insuficiente");
		
	}

}
