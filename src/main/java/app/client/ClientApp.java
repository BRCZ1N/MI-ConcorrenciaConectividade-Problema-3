package app.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import app.model.AccountModel;
import app.model.BalanceModel;
import app.model.BankModel;
import app.model.DepositModel;
import app.model.OperationsModel;
import app.model.TransferModel;
import app.model.UserModel;
import app.utilities.BanksEnum;
import application.exceptions.UnableToConnectException;

public class ClientApp {
	private boolean connected;
	private boolean log;
	private boolean pick;
	private Scanner scanner = new Scanner(System.in);
	private String escolha;
	private BankModel bankIp;
	private AccountModel user;
	private AccountModel userReceptor;
	private OperationsModel operation;
	private DepositModel deposit;
	private TransferModel transfer;
	private BalanceModel balance;

	private UserModel beneficiareUser;

	private void execBank() throws IOException, UnableToConnectException {
		boolean connectedBank = true;
		while (connectedBank) {
			pickBank();
			loginClient();
			menuClient();
		}

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

						bankIp = setBank(banco);
						pick = false;
					} else if (escolha.equals("2")) {
						System.out.println("Escolha outro banco");

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
				// aqui transfere a nova conta pro servidor
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
			String id = scanner.next();
			System.out.println("====== Digite sua senha: ======");
			String senha = scanner.next();
			user = new AccountModel(id, senha, bankIp);
			//fazer requisição e condicionais 
		}
	}

	private void menuClient() {
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

	private void depositMoney() {
		System.out.println("==================== Deposito =====================");
		System.out.println("===================================================");
		System.out.println("====== Digite o valor que deseja depositar:");
		Double value = scanner.nextDouble();
		deposit = new DepositModel(user, value);
	}

	private void transferMoney() {
		Boolean transferLoop = true;
		System.out.println("==================== Transferencia =====================");
		System.out.println("===================================================");
		System.out.println("====== Digite o valor que deseja Transferir:");
		Double value = scanner.nextDouble();
		System.out.println("====== Digite o id da conta de destino:");
		String id = scanner.next();
		while(transferLoop) {
			System.out.println("====== Escolha o banco de destino: ======");
			System.out.println("====== (1) - Banco 1");
			System.out.println("====== (2) - Banco 2");
			System.out.println("====== (3) - Banco 3");
			System.out.println("====== (4) - Banco 4");
			String banco = scanner.next();
			if (banco.equals("1") || banco.equals("2") || banco.equals("3") || banco.equals("4")) {
				BankModel receptorBank = setBank(banco);
				userReceptor = new AccountModel(id,receptorBank);
				transfer = new TransferModel(user, userReceptor, value);
				
			}else {
				System.out.println("====== Escolha um banco correto ======");
			}
		}
		

	}

	private void consultBalance() {
		balance = new BalanceModel(user);
	}

	public static void main(String[] args) throws IOException, UnableToConnectException {

		ClientApp appBank = new ClientApp();
		appBank.execBank();

	}
}
