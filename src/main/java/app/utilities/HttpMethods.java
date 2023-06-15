package app.utilities;

/**
 * Esta � a enumera��o HttpCodes, que possui os metodos das requisi��es http
 * 
 * @version 1.0
 */
public enum HttpMethods {

	GET("GET"), PUT("PUT"), POST("POST"), DELETE("DELETE");

	private String method;

	/**
	 * Esse � o construtor da enumera��o HttpMethods, que constroe o objeto que
	 * representa os possiveis metodos http
	 * 
	 * @param String method
	 */
	private HttpMethods(String method) {

		this.method = method;

	}

	/**
	 * Esse � o m�todo, que retorna c�digo do metodo http
	 * 
	 * @return Metodo http
	 */
	public String getMethod() {
		return method;
	}

}
