package app.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Classe responsável por enviar as requisições a outro banco 
 */
public class Http {
	
	/**
	 * Envia uma requisição HTTP e retorna a resposta HTTP correspondente em um objeto ResponseHttp.
	 * 
	 * @param requestHttp objeto RequestHttp contendo os dados da requisição
	 * @param ip o endereço IP ou URL do servidor para o qual a requisição será enviada
	 * @return um objeto ResponseHttp contendo a resposta HTTP, ou null se ocorrer um erro ao enviar a requisição ou receber a resposta
	 * @throws IOException se ocorrer um erro ao enviar a requisição ou receber a resposta
	 */
	public static ResponseHttp sendHTTPRequestAndGetHttpResponse(RequestHttp requestHttp, String ip) throws IOException  {

//		OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(5,TimeUnit.SECONDS).build();
		OkHttpClient client = new OkHttpClient();
		String url = ip + requestHttp.getPath();
		Request request = new Request.Builder().url(url).method(requestHttp.getMethod(),(requestHttp.getBody() == null ? null : RequestBody.create(requestHttp.getBody().getBytes("UTF-8")))).headers(Headers.of(requestHttp.getHeaders())).build();
		Response response = client.newCall(request).execute();
		ResponseHttp responseHttp = formatHTTPResponse(response);

		return responseHttp;

	}
	/**
	 * Formata uma resposta HTTP em um objeto ResponseHttp contendo os dados da resposta.
	 * 
	 * @param responseHttp a resposta HTTP a ser formatada
	 * @return um objeto ResponseHttp contendo os dados da resposta formatada
	 * @throws IOException se ocorrer]
	 */
	public static ResponseHttp formatHTTPResponse(Response responseHttp) throws IOException {

		Map<String, String> headersMap = new HashMap<String, String>();

		for (int i = 0, size = responseHttp.headers().size(); i < size; i++) {

			String name = responseHttp.headers().name(i);
			String value = responseHttp.headers().value(i);
			headersMap.put(name, value);

		}
		
		ResponseHttp response = new ResponseHttp(HttpCodes.valueOf("HTTP_" + responseHttp.code()).getCodeHttp(),headersMap, responseHttp.body().string());

		return response;

	}

}
