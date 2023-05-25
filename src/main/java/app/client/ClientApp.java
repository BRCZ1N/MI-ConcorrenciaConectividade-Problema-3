package app.client;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import application.car.CarApp;
import application.exceptions.UnableToConnectException;
import utilityclasses.ConfigLarsidIpsHttp;
import utilityclasses.Http;
import utilityclasses.RequestHttp;
import utilityclasses.ResponseHttp;

public class ClientApp {
	private boolean connected = true;
	private boolean log = true;
	private boolean pick = true;
	private Scanner scanner = new Scanner(System.in);
	private String banco;
	private String escolha;
	private volatile String bankIp;
	private void execBank() throws IOException, UnableToConnectException {
		pickBank();
		loginClient();
		menuClient();

	}

	private void pickBank() {
		while(pick) {
			System.out.println("===================================================");
			System.out.println("========= Bancos Disponiveis ==========");
			System.out.println("===================================================");
			System.out.println("====== Escolha o banco: ======");
			System.out.println("====== (1) - Banco 1");
			System.out.println("====== (2) - Banco 2");
			System.out.println("====== (3) - Banco 3");
			System.out.println("====== (4) - Banco 4");
			banco = scanner.next();
			if (banco.equals("1") || banco.equals("2") || banco.equals("3") || banco.equals("4")) {
				while(pick){
					escolha = changeGo(banco);
					if(escolha.equals("1")) {
						 
						 if(banco.equals("1")) {
								
								bankIp = ConfigLarsidIpsHttp.HTTP_FOG_REGION_Q1.getAddress();
							}else if(banco.equals("2")) {
								
								bankIp = ConfigLarsidIpsHttp.HTTP_FOG_REGION_Q2.getAddress();
							}else if(banco.equals("3")) {
								
								bankIp = ConfigLarsidIpsHttp.HTTP_FOG_REGION_Q3.getAddress();
							}else if(banco.equals("4")) {
								
								bankIp = ConfigLarsidIpsHttp.HTTP_FOG_REGION_Q4.getAddress();
							}
						 pick = false;
					}else if(escolha.equals("2")) {
						System.out.println("Escolha outro banco");
					}else {
						System.out.println("Digite uma opção correta");
						escolha = changeGo(banco);
					}
		           
				}
	            
	        } else {
	            System.out.println("Banco invalido");
	        }
		}
		
	}

	private String changeGo(String banco) {
		
		System.out.println("Banco selecionado: " + "BANCO"+""+banco);
        System.out.println("========= Pagina Inicial ==========" );
        System.out.println("===================================================");
        System.out.println("====== (1) - Tela de Login");
		System.out.println("====== (2) - Alterar banco");
		System.out.println("===================================================");
		String escolha = scanner.next();
		return escolha;
		
	}

	private void loginClient() throws IOException {
		while (log) {
			System.out.println("===================================================");
			System.out.println("========= Central bancaria:"+ banco +" ==========");
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
			JSONObject json) throws IOException {

		ResponseHttp response = Http.sendHTTPRequestAndGetHttpResponse(
				new RequestHttp(method, endpoint, httpVersion, header),json);
		return response;

	}
	public static void main(String[] args) throws IOException, UnableToConnectException {

		ClientApp appBank = new ClientApp();
		appBank.execBank();

	}
}
