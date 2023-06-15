package app.exceptions;

/**
 * Classe da exceção lançada quando ocorre uma falha na conexão com o servidor.
 */
public class ServerConnectionException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Cria uma nova instância de ServerConnectionException com a mensagem padrão "A conexão falhou, tente novamente".
	 */
	public ServerConnectionException() {
		
		super("A conexão falhou, tente novamente");

	}

}
