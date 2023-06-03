package app.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.http.HttpStatus;

import app.exceptions.ServerConnectionException;
import app.model.AccountModel;
import app.model.BankModel;
import app.model.DepositModel;
import app.model.LoginAccountModel;
import app.model.OperationAccountModel;
import app.model.TransferModel;
import app.model.UserModel;
import app.utilities.BanksEnum;
import app.utilities.Http;
import app.utilities.HttpMethods;
import app.utilities.RequestHttp;
import app.utilities.ResponseHttp;
import io.netty.handler.codec.http.HttpVersion;

public class ClientApp {
	private boolean connected;
	private boolean log = true;
	private boolean pick;

	private Scanner scanner = new Scanner(System.in);
	private String escolha;
	private BankModel bankIp;
	private AccountModel user;
	private TransferModel transfer;
	private OperationAccountModel operationAccount;
	private LoginAccountModel userLogin;
	private UserModel beneficiareUser;
	private String id;
	private String password;

	private void execBank() throws IOException, ServerConnectionException {
		boolean connectedBank = true;
		while (connectedBank) {
			pickBank();
			loginClient();
			menuClient();
		}

	}

	private void pickBank() throws ServerConnectionException {
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
				boolean picktwo = true;
				while (picktwo) {
					escolha = changeGo(banco);
					if (escolha.equals("1")) {

						bankIp = setBank(banco);
						picktwo = false;
						pick = false;

					} else if (escolha.equals("2")) {
						System.out.println("Escolha outro banco");
						picktwo = false;
					} else if (escolha.equals("3")) {
						bankIp = setBank(banco);
						registerUser(bankIp);

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

	private BankModel setBank(String banco) {
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
	 * @throws ServerConnectionException
	 */
	private void registerUser(BankModel ipBank) throws ServerConnectionException {

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
				RequestHttp request;
				ResponseHttp response;
				user = new AccountModel(senhaRegister, ipBank, beneficiares);
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", "application/json");

				try {

					request = new RequestHttp(HttpMethods.POST.getMethod(), "/account/create",
							HttpVersion.HTTP_1_1.toString(), header, user.toJSON());
					response = Http.sendHTTPRequestAndGetHttpResponse(request, ipBank.getIp());

				} catch (IOException e) {

					throw new ServerConnectionException();

				}
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

	private void loginClient() throws IOException, ServerConnectionException {
		while (log) {
			System.out.println("===================================================");
			System.out.println("========= Central bancaria: ==========");
			System.out.println("===================================================");
			System.out.println("====== Digite seu id: ======");
			id = scanner.next();
			System.out.println("====== Digite sua senha: ======");
			password = scanner.next();
			userLogin = new LoginAccountModel(id, password);
			operationAccount = new OperationAccountModel(id, bankIp);
			// fazer requisição e condicionais
			RequestHttp request;
			ResponseHttp response;
			Map<String, String> header = new HashMap<String, String>();
			header.put("Content-Type", "application/json");

			try {

				request = new RequestHttp(HttpMethods.PUT.getMethod(), "/auth", HttpVersion.HTTP_1_1.toString(), header,
						userLogin.toJSON());
				response = Http.sendHTTPRequestAndGetHttpResponse(request, bankIp.getIp());

			} catch (IOException e) {

				throw new ServerConnectionException();

			}
			if (response.getStatusLine().contains(HttpStatus.OK.getReasonPhrase())) {
				log = false;

			}
			System.out.println(response.getBody());

		}
	}

	private void menuClient() throws ServerConnectionException {
		while (connected) {

			System.out.println("================ Menu de cliente ==================");
			System.out.println("===================================================");
			System.out.println("====== (1) - Consultar Saldo");
			System.out.println("====== (2) - Transferir dinheiro");
			System.out.println("====== (3) - Depositar dinheiro");
			System.out.println("====== (4) - Desconectar");
			System.out.println("=========== Digite a opcao desejada ===============");
			String opcao = scanner.next();
			if (opcao.equals("1") || opcao.equals("2") || opcao.equals("3") || opcao.equals("4")) {
				if (opcao.equals("1")) {
					consultBalance();
				} else if (opcao.equals("2")) {
					transferMoney();
				} else if (opcao.equals("3")) {
					depositMoney();
				} else {
					connected = false;
				}

			} else {
				System.out.println("================ Digite uma opção correta ==================");
			}
		}
	}

	private void depositMoney() throws ServerConnectionException {
		System.out.println("==================== Deposito =====================");
		System.out.println("===================================================");
		System.out.println("====== Digite o valor que deseja depositar:");
		Double value = scanner.nextDouble();
		DepositModel deposit = new DepositModel(operationAccount, value);
		RequestHttp request;
		ResponseHttp response;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		try {

			request = new RequestHttp(HttpMethods.PUT.getMethod(), "/deposit", HttpVersion.HTTP_1_1.toString(), header,
					deposit.toJSON());
			response = Http.sendHTTPRequestAndGetHttpResponse(request, bankIp.getIp());

		} catch (IOException e) {

			throw new ServerConnectionException();

		}if (response.getStatusLine().contains(HttpStatus.OK.getReasonPhrase())) {
			System.out.println(response.getBody());

		}
		System.out.println(response.getBody());

	}

	private void transferMoney() throws ServerConnectionException {
		Boolean transferLoop = true;
		System.out.println("==================== Transferencia =====================");
		System.out.println("===================================================");
		System.out.println("====== Digite o valor que deseja Transferir:");
		Double value = scanner.nextDouble();
		System.out.println("====== Digite o id da conta de destino:");
		String idReceptor = scanner.next();
		while (transferLoop) {
			System.out.println("====== Escolha o banco de destino: ======");
			System.out.println("====== (1) - Banco 1");
			System.out.println("====== (2) - Banco 2");
			System.out.println("====== (3) - Banco 3");
			System.out.println("====== (4) - Banco 4");
			String banco = scanner.next();
			if (banco.equals("1") || banco.equals("2") || banco.equals("3") || banco.equals("4")) {
				BankModel receptorBank = setBank(banco);
				OperationAccountModel userReceptor = new OperationAccountModel(idReceptor, receptorBank);
				OperationAccountModel userInitial = new OperationAccountModel(id, bankIp);
				transfer = new TransferModel(userInitial, userReceptor, value);
				RequestHttp request;
				ResponseHttp response;
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", "application/json");

				try {

					request = new RequestHttp(HttpMethods.PUT.getMethod(), "/transfer", HttpVersion.HTTP_1_1.toString(),
							header, transfer.toJSON());
					response = Http.sendHTTPRequestAndGetHttpResponse(request, bankIp.getIp());

				} catch (IOException e) {

					throw new ServerConnectionException();

				}
				if (response.getStatusLine().contains(HttpStatus.OK.getReasonPhrase())) {
					System.out.println(response.getBody());

				}
				System.out.println(response.getBody());
			} else {
				System.out.println("====== Escolha um banco correto ======");
			}
		}

	}

	private void consultBalance() throws ServerConnectionException {
		RequestHttp request;
		ResponseHttp response;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		try {

			request = new RequestHttp(HttpMethods.GET.getMethod(), "/balance", HttpVersion.HTTP_1_1.toString(), header);
			response = Http.sendHTTPRequestAndGetHttpResponse(request, bankIp.getIp());

		} catch (IOException e) {

			throw new ServerConnectionException();

		}if (response.getStatusLine().contains(HttpStatus.OK.getReasonPhrase())) {
			System.out.println(response.getBody());

		}
		System.out.println(response.getBody());
	}

	public static void main(String[] args) throws IOException, ServerConnectionException {

		ClientApp appBank = new ClientApp();
		appBank.execBank();

	}
}
