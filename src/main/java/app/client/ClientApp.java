package app.client;

import org.json.JSONArray;
import org.json.JSONObject;

import app.model.AccountModel;
import app.model.BankModel;
import app.model.UserModel;
import app.utilities.BanksEnum;

import java.io.IOException;
import java.util.ArrayList;
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
	private boolean log;
	private boolean pick;
	private Scanner scanner = new Scanner(System.in);
	private String escolha;
	private volatile BankModel bankIp;
	private AccountModel user;
	private UserModel beneficiareUser;

	private void execBank() throws IOException, UnableToConnectException {
		pickBank();
		loginClient();
		menuClient();

	}

	private void pickBank() {
		while (pick) {
			System.out.println("===================================================");
			System.out.println("========= Bancos Disponiveis ==========");
			System.out.println("===================================================");
			System.out.println("====== Escolha o banco: ======");
			System.out.println("====== (1) - Banco 1");
			System.out.println("====== (2) - Banco 2");
			System.out.println("====== (3) - Banco 3");
			System.out.println("====== (4) - Banco 4");
			String banco = scanner.next();
			if (banco.equals("1") || banco.equals("2") || banco.equals("3") || banco.equals("4")) {
				while (pick) {
					escolha = changeGo(banco);
					if (escolha.equals("1")) {

						BankModel ipBank = setIpBank(banco);
						pick = false;
					} else if (escolha.equals("2")) {
						System.out.println("Escolha outro banco");

					} else if (escolha.equals("3")) {
						BankModel ipBank = setIpBank(banco);
						registerUser(ipBank);

					} else {
						System.out.println("Digite uma opção correta");
						escolha = changeGo(banco);
					}

				}

			} else {
				System.out.println("Banco invalido");
			}
		}

	}

	private BankModel setIpBank(String banco) {
		if (banco.equals("1")) {

			bankIp = BanksEnum.BANK_1.getBank();
		} else if (banco.equals("2")) {

			bankIp = BanksEnum.BANK_2.getBank();
		} else if (banco.equals("3")) {

			bankIp = BanksEnum.BANK_3.getBank();
		} else if (banco.equals("4")) {

			bankIp = BanksEnum.BANK_4.getBank();
		}
		return bankIp;

	}

	/**
	 * @param ipBank
	 */
	private void registerUser(BankModel ipBank) {

		ArrayList<UserModel> beneficiares = new ArrayList<UserModel>();
		boolean register = true;
		int count = 0;
		while (register) {
			System.out.println("====== Digite a senha desejada: ======");
			String senhaRegister = scanner.next();
			System.out.println("====== Deseja Cadastrar quantos beneficiarios: ======");
			int countBeneficiares = scanner.nextInt();

			if (countBeneficiares > 1) {

				while (countBeneficiares != count) {
					System.out.println("====== Digite o nome do beneficiario: ======");
					String beneficiareName = scanner.next();
					System.out.println("====== Digite o cpf do beneficiario: ======");
					String beneficiareCpf = scanner.next();

					beneficiareUser = new UserModel(beneficiareName, beneficiareCpf);
					beneficiares.add(beneficiareUser);

					count += 1;
				}
				user = new AccountModel(senhaRegister, ipBank, beneficiares);
				register = false;
			} else {
				System.out.println("Esta conta devera ter ao menos um beneficiario");
			}
			
		}

	}

	private String changeGo(String banco) {

		System.out.println("Banco selecionado: " + "BANCO" + "" + banco);
		System.out.println("========= Pagina Inicial ==========");
		System.out.println("===================================================");
		System.out.println("====== (1) - Tela de Login");
		System.out.println("====== (2) - Alterar banco");
		System.out.println("====== (3) - Criar conta");
		System.out.println("===================================================");
		String escolha = scanner.next();
		return escolha;

	}

	private void loginClient() throws IOException {
		while (log) {
			System.out.println("===================================================");
			System.out.println("========= Central bancaria: ==========");
			System.out.println("===================================================");
			System.out.println("====== Digite seu id: ======");
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

		ResponseHttp response = Http
				.sendHTTPRequestAndGetHttpResponse(new RequestHttp(method, endpoint, httpVersion, header), json);
		return response;

	}

	public static void main(String[] args) throws IOException, UnableToConnectException {

		ClientApp appBank = new ClientApp();
		appBank.execBank();

	}
}
