package app.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import application.exceptions.UnableToConnectException;
import utilityclasses.Http;
import utilityclasses.RequestHttp;
import utilityclasses.ResponseHttp;

public class ClientApp {
	private boolean connected = true;
	private boolean log = true;
	private Scanner scanner = new Scanner(System.in);

	private void execCar() throws IOException, UnableToConnectException {

		loginClient();
		menuClient();

	}

	private void loginClient() throws IOException {
		while (log) {
			System.out.println("===================================================");
			System.out.println("========= Central bancaria ==========");
			System.out.println("===================================================");
			System.out.println("====== Digite seu cpf: ======");
			String cpf = scanner.next();
			System.out.println("====== Digite sua senha: ======");
			String senha = scanner.next();
			Map<String, String> header = new HashMap<String, String>();
			header.put("Content-Lenght", "0");
			JSONObject json = new JSONObject();
			json.put("cpf", cpf);
			json.put("senha", senha);
			ResponseHttp response;
			response = messageReturn("GET", "/station/shorterQueue", "HTTP/1.1", header, json);
		}
	}

	private void menuClient() {
		while (connected) {

			System.out.println("================ Menu de cliente ==================");
			System.out.println("===================================================");
			System.out.println("====== (1) - Consultar Saldo");
			System.out.println("====== (2) - Transferir dinheiro");
			System.out.println("====== (3) - Desconectar");
			System.out.println("=========== Digite a opcao desejada ===============");
			String opcao = scanner.next();
		}
	}

	public ResponseHttp messageReturn(String method, String endpoint, String httpVersion, Map<String, String> header,
			String json) throws IOException {

		ResponseHttp response = Http.sendHTTPRequestAndGetHttpResponse(
				new RequestHttp(method, endpoint, httpVersion, header,json));
		return response;

	}
}
