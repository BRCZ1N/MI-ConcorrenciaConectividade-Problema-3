package app.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.http.HttpVersion;
import com.google.gson.Gson;

import app.exceptions.ServerConnectionException;
import app.model.AccountModel;
import app.model.BankModel;
import app.model.DepositModel;
import app.model.LoginAccountModel;
import app.model.MessageModel;
import app.model.OperationAccountModel;
import app.model.TransferModel;
import app.model.UserModel;
import app.utilities.BanksEnum;
import app.utilities.Http;
import app.utilities.HttpCodes;
import app.utilities.HttpMethods;
import app.utilities.RequestHttp;
import app.utilities.ResponseHttp;

/**
 * Classe responsável por simular um cliente do banco. Permite ao usuário realizar operações bancárias, como login, criar conta, consultar saldo, transferir saldo e depositar saldo.
 */
public class ClientApp {
	
	private Scanner scanner = new Scanner(System.in);
	private BankModel bankCurrent;
	private AccountModel user;
	private OperationAccountModel operationAccount;
	private LoginAccountModel userLogin;
	private UserModel beneficiareUser;

	/**
	 * Método principal que executa a simulação do cliente de banco.
	 * @throws ServerConnectionException Se ocorrer um erro na conexão com o servidor.
	 * @throws IOException Se ocorrer um erro de I/O durante a execução.
	 */
	private void execBank() throws ServerConnectionException, IOException {

		boolean pick = true;

		bankCurrent = selectBank();
		
		while (pick) {

			System.out.println("Banco selecionado: " + "BANCO - " + bankCurrent.getId());
			System.out.println("================== Pagina Inicial =================");
			System.out.println("===================================================");
			System.out.println("====== (1) - Tela de Login");
			System.out.println("====== (2) - Alterar banco");
			System.out.println("====== (3) - Criar conta");
			System.out.println("===================================================");
			String choice = scanner.next();

			if (choice.equals("1")) {

				if(loginClient()) {
					
					menuClient();

				}

			} else if (choice.equals("2")) {

				bankCurrent = selectBank();

			} else if (choice.equals("3")) {

				registerUser();

			} else {

				System.out.println("Digite uma opção válida");

			}

		}

	}

	/**
	 * Permite ao usuário selecionar um banco.
	 * @return O objeto BankModel representando o banco selecionado.
	 */
	public BankModel selectBank() {

		boolean choice = true;
		BankModel bank = new BankModel();

		do {

			System.out.println("===================================================");
			System.out.println("================ Bancos Disponiveis ===============");
			System.out.println("===================================================");
			System.out.println("================= Escolha o banco: ================");
			System.out.println("====== (1) - Banco 1");
			System.out.println("====== (2) - Banco 2");
			System.out.println("====== (3) - Banco 3");
			System.out.println("====== (4) - Banco 4");
			String option = scanner.next();
			System.out.println("===================================================");

			if (option.equals("1")) {

				bank = BanksEnum.BANK_1.getBank();
				choice = false;

			} else if (option.equals("2")) {

				bank = BanksEnum.BANK_2.getBank();
				choice = false;

			} else if (option.equals("3")) {

				bank = BanksEnum.BANK_3.getBank();
				choice = false;

			} else if (option.equals("4")) {

				bank = BanksEnum.BANK_4.getBank();
				choice = false;

			} else {

				System.out.println("Banco escolhido incorretamente tente novamente");
			}

		} while (choice);

		return bank;

	}

	/**
	 * Registra um novo usuário no banco.
	 * @throws ServerConnectionException se ocorrer um erro de conexão com o servidor.
	 */
	private void registerUser() throws ServerConnectionException {

		ArrayList<UserModel> beneficiares = new ArrayList<UserModel>();
		boolean register = false;

		while (!register) {

			System.out.println("===================================================");
			System.out.println("============ Digite a senha desejada:  ============");
			String senhaRegister = scanner.next();
			System.out.println("===================================================");

			int amountBeneficiares = 0;

			do {

				try {

					System.out.println("===================================================");
					System.out.println("==== Digite a quantidade de beneficiarios: ========");
					amountBeneficiares = scanner.nextInt();
					System.out.println("===================================================");

					if (amountBeneficiares <= 0) {

						System.out.println("A conta devera ter ao menos um beneficiario");

					}

				} catch (NumberFormatException e) {
					
					System.out.println("A quantidade foi digitada incorretamente");

				}

			} while (amountBeneficiares <= 0);

			for (int i = 0; i < amountBeneficiares; i++) {

				System.out.println("===========================================");
				System.out.println("====== Digite o nome do beneficiario: =====");
				String beneficiareName = scanner.next();
				System.out.println("===========================================");

				System.out.println("===========================================");
				System.out.println("====== Digite o cpf do beneficiario: ======");
				String beneficiareCpf = scanner.next();
				System.out.println("===========================================");

				beneficiareUser = new UserModel(beneficiareName, beneficiareCpf);
				beneficiares.add(beneficiareUser);

			}

			RequestHttp request;
			ResponseHttp response;
			user = new AccountModel(senhaRegister, bankCurrent, beneficiares);
			Map<String, String> header = new HashMap<String, String>();
			header.put("Content-Type", "application/json");

			try {

				request = new RequestHttp(HttpMethods.POST.getMethod(), "/account/create",HttpVersion.HTTP_1_1.toString(), header, user.toJSON());
				response = Http.sendHTTPRequestAndGetHttpResponse(request, bankCurrent.getIp());
				
			} catch (IOException e) {

				throw new ServerConnectionException();

			}

			Gson gson = new Gson();
			MessageModel message = gson.fromJson(response.getBody(), MessageModel.class);
			System.out.println(message.getMessage());
			
		
			if (response.getStatusLine().equals(HttpCodes.HTTP_201.getCodeHttp())) {

				register = true;

			}else {
				
				register = false;
			}

		}

	}

	/**
	 * Realiza o login do cliente.
	 * @return true se o login for bem-sucedido, false caso contrário.
	 * @throws IOException se ocorrer um erro de entrada/saída.
	 * @throws ServerConnectionException se ocorrer um erro de conexão com o servidor.
	 */
	private boolean loginClient() throws IOException, ServerConnectionException {

		System.out.println("===================================================");
		System.out.println("=============== Central bancaria: =================");
		System.out.println("===================================================");
		System.out.println();
		System.out.println("===================================================");
		System.out.println("================ Digite seu id: ===================");
		String id = scanner.next();
		System.out.println("================ Digite sua senha: ================");
		String password = scanner.next();
		System.out.println("===================================================");

		userLogin = new LoginAccountModel(id, password);
		RequestHttp request;
		ResponseHttp response;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		try {

			request = new RequestHttp(HttpMethods.POST.getMethod(), "/account/auth", HttpVersion.HTTP_1_1.toString(),header, userLogin.toJSON());
			response = Http.sendHTTPRequestAndGetHttpResponse(request, bankCurrent.getIp());

		} catch (IOException e) {

			throw new ServerConnectionException();

		}

		Gson gson = new Gson();
		MessageModel message = gson.fromJson(response.getBody(), MessageModel.class);
		System.out.println(message.getMessage());

		if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

			return true;

		}

		return false;

	}

	/**
	 * Exibe o menu principal do cliente.
	 * @throws ServerConnectionException se ocorrer um erro de conexão com o servidor.
	 */
	private void menuClient() throws ServerConnectionException {

		boolean connected = true;
		
		while (connected) {

			System.out.println("================ Menu de cliente ==================");
			System.out.println("===================================================");
			System.out.println("====== (1) - Consultar Saldo");
			System.out.println("====== (2) - Transferir Saldo");
			System.out.println("====== (3) - Depositar Saldo");
			System.out.println("====== (4) - Desconectar");
			System.out.println("=========== Digite a opcao desejada ===============");
			String opcao = scanner.next();

			if (opcao.equals("1") || opcao.equals("2") || opcao.equals("3") || opcao.equals("4")) {

				if (opcao.equals("1")) {

					consultBalance();

				} else if (opcao.equals("2")) {

					transferBalance();

				} else if (opcao.equals("3")) {

					depositBalance();

				} else {

					connected = false;

				}

			} else {
				
				System.out.println("================ Digite uma opção correta ==================");
				
			}
		}
	}

	/**
	 * Realiza um depósito na conta do cliente.
	 * @throws ServerConnectionException se ocorrer um erro de conexão com o servidor.
	 */
	private void depositBalance() throws ServerConnectionException {

		System.out.println("==================== Deposito =====================");
		System.out.println("===================================================");
		System.out.println("====== Digite o valor que deseja depositar: =======");
		Double value = scanner.nextDouble();
		System.out.println("===================================================");

		operationAccount = new OperationAccountModel(userLogin.getId(), bankCurrent);
		DepositModel deposit = new DepositModel(operationAccount, value);
		
		RequestHttp request;
		ResponseHttp response;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		try {

			request = new RequestHttp(HttpMethods.POST.getMethod(), "/account/deposit", HttpVersion.HTTP_1_1.toString(), header,deposit.toJSON());
			response = Http.sendHTTPRequestAndGetHttpResponse(request, bankCurrent.getIp());

		} catch (IOException e) {

			throw new ServerConnectionException();

		}

		Gson gson = new Gson();
		MessageModel message = gson.fromJson(response.getBody(), MessageModel.class);
		System.out.println(message.getMessage());

	}

	/**
	 * Realiza uma transferência de saldo para outra conta.
	 * @throws ServerConnectionException se ocorrer um erro de conexão com o servidor.
	 */
	private void transferBalance() throws ServerConnectionException {

		System.out.println("==================== Transferencia =====================");
		System.out.println("========================================================");
		System.out.println("======= Digite o valor que deseja transferir: ==========");
		Double value = scanner.nextDouble();
		System.out.println("========================================================");
		System.out.println("======= Digite o id da conta de destino: ===============");
		String idReceptor = scanner.next();
		System.out.println("========================================================");

		System.out.println("=========== Escolha um banco de destino ============");
		BankModel receptorBank = selectBank();
		OperationAccountModel userReceptor = new OperationAccountModel(idReceptor, receptorBank);
		OperationAccountModel userInitial = new OperationAccountModel(userLogin.getId(), bankCurrent);

		TransferModel transfer = new TransferModel(userInitial, userReceptor, value);

		RequestHttp request;
		ResponseHttp response;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		try {

			request = new RequestHttp(HttpMethods.POST.getMethod(), "/account/transfer", HttpVersion.HTTP_1_1.toString(), header,transfer.toJSON());
			response = Http.sendHTTPRequestAndGetHttpResponse(request, bankCurrent.getIp());

		} catch (IOException e) {

			throw new ServerConnectionException();

		}

		Gson gson = new Gson();
		MessageModel message = gson.fromJson(response.getBody(), MessageModel.class);
		System.out.println(message.getMessage());

	}

	/**
	 * Consulta o saldo da conta do cliente.
	 * @throws ServerConnectionException se ocorrer um erro de conexão com o servidor.
	 */
	private void consultBalance() throws ServerConnectionException {

		RequestHttp request;
		ResponseHttp response;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		try {

			request = new RequestHttp(HttpMethods.GET.getMethod(), "/account/balance?id="+userLogin.getId(), HttpVersion.HTTP_1_1.toString(), header);
			response = Http.sendHTTPRequestAndGetHttpResponse(request, bankCurrent.getIp());

		} catch (IOException e) {

			throw new ServerConnectionException();

		}

		Gson gson = new Gson();
		MessageModel message = gson.fromJson(response.getBody(), MessageModel.class);
		System.out.println(message.getMessage());
	}

	/**
	 * Método principal para execução do cliente bancário.
	 * @param args os argumentos de linha de comando.
	 * @throws IOException se ocorrer um erro de entrada/saída.
	 * @throws ServerConnectionException se ocorrer um erro de conexão com o servidor.
	 */
	public static void main(String[] args) throws IOException, ServerConnectionException {

		ClientApp appBank = new ClientApp();
		appBank.execBank();

	}
}
