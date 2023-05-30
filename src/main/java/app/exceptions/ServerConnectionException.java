package app.exceptions;

public class ServerConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerConnectionException() {
		
		super("A conexão com o outro banco falhou, tente novamente");

	}

}
