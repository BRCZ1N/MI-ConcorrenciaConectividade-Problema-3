package app.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpVersion;
import org.springframework.stereotype.Component;

import app.exceptions.InsufficientBalanceException;
import app.exceptions.ServerConnectionException;
import app.model.AccountModel;
import app.model.DepositModel;
import app.model.LoginAccountModel;
import app.model.TransferModel;
import app.utilities.Http;
import app.utilities.HttpCodes;
import app.utilities.HttpMethods;
import app.utilities.RequestHttp;
import app.utilities.ResponseHttp;

@Component
/**
 * Classe responsável por representar os serviços de conta
 */
public class AccountServices {

	private Map<String, AccountModel> accounts;
	private long id = 0;

	/**
	 * Construtor da classe AccountServices.
	 */
	public AccountServices() {

		this.setAccounts(new ConcurrentHashMap<String, AccountModel>());

	}

	/**
	 * Obtém a lista de contas.
	 * 
	 * @return A lista de contas.
	 */
	public Map<String, AccountModel> getAccounts() {
		return accounts;
	}

	/**
	 * Define a lista de contas.
	 * 
	 * @param accounts A lista de contas.
	 */
	public void setAccounts(Map<String, AccountModel> accounts) {
		this.accounts = accounts;
	}

	/**
	 * Cria uma nova conta.
	 * 
	 * @param client O objeto AccountModel representando o cliente.
	 * @return Um Optional contendo a nova conta criada, ou Optional.empty() caso a
	 *         conta já exista.
	 */
	public Optional<AccountModel> createAccount(AccountModel client) {

		if (accounts.containsValue(client)) {

			return Optional.empty();

		}

		client.setId(String.valueOf(id));
		accounts.put(String.valueOf(id), client);
		id += 1;

		return Optional.of(client);

	}

	/**
	 * Realiza uma operação de depósito em uma conta.
	 * 
	 * @param deposit O objeto DepositModel representando a operação de depósito.
	 * @return Um Optional contendo a conta atualizada após o depósito, ou
	 *         Optional.empty() caso a conta não seja encontrada.
	 */
	public Optional<AccountModel> depositOperation(DepositModel deposit) {

		Optional<AccountModel> resultSearch = findById(deposit.getAccountOrigin().getIdAccount());

		if (resultSearch.isEmpty()) {

			return resultSearch;

		}

		AccountModel clientAccount = resultSearch.get();
		clientAccount.setBalance(clientAccount.getBalance() + deposit.getValue());
		accounts.replace(clientAccount.getId(), clientAccount);
		return Optional.of(clientAccount);

	}

	/**
	 * Obtém o saldo de uma conta.
	 * 
	 * @param id O ID da conta.
	 * @return Um Optional contendo a conta encontrada, ou Optional.empty() caso a
	 *         conta não seja encontrada.
	 */
	public Optional<AccountModel> getBalanceOperation(String id) {

		Optional<AccountModel> resultSearch = findById(id);

		if (resultSearch.isEmpty()) {

			return resultSearch;

		}

		return Optional.of(resultSearch.get());

	}

	/**
	 * Realiza uma operação de transferência entre contas.
	 * 
	 * @param transfer O objeto TransferModel representando a operação de
	 *                 transferência.
	 * @return Um Optional contendo a conta de origem atualizada após a transferência,
	 *         ou Optional.empty() caso ocorra algum erro na transferência.
	 * @throws InsufficientBalanceException   Se a conta de origem não possuir saldo
	 *                                        suficiente para a transferência.
	 * @throws ServerConnectionException      Se ocorrer um erro na conexão com o
	 *                                        servidor da conta de destino.
	 */
	public Optional<AccountModel> transferOperation(TransferModel transfer) throws InsufficientBalanceException, ServerConnectionException {

		AccountModel accountOrigin;
		AccountModel accountDestiny;
		Optional<AccountModel> resultSearchAccountOrigin;
		Optional<AccountModel> resultSearchAccountDestiny;

		if (transfer.getAccountOrigin().getBank().getId().equals(transfer.getAccountDestiny().getBank().getId())) {

			resultSearchAccountOrigin = findById(transfer.getAccountOrigin().getIdAccount());
			resultSearchAccountDestiny = findById(transfer.getAccountDestiny().getIdAccount());

			if (resultSearchAccountOrigin.isEmpty() || resultSearchAccountDestiny.isEmpty()) {

				return Optional.empty();

			} else {

				accountOrigin = resultSearchAccountOrigin.get();
				accountDestiny = resultSearchAccountDestiny.get();

				if (accountOrigin.getBalance() < transfer.getValue()) {

					throw new InsufficientBalanceException();

				} else {

					accountOrigin.setBalance(accountOrigin.getBalance() - transfer.getValue());
					accountDestiny.setBalance(accountDestiny.getBalance() + transfer.getValue());

					accounts.replace(accountOrigin.getId(), accountOrigin);
					accounts.replace(accountDestiny.getId(), accountDestiny);

					return Optional.of(accountOrigin);

				}
			}

		} else {

			resultSearchAccountOrigin = findById(transfer.getAccountOrigin().getIdAccount());

			if (resultSearchAccountOrigin.isEmpty()) {

				return Optional.empty();

			}

			accountOrigin = resultSearchAccountOrigin.get();

			if (accountOrigin.getBalance() < transfer.getValue()) {

				throw new InsufficientBalanceException();

			}

			RequestHttp request;
			ResponseHttp response;
			DepositModel deposit = new DepositModel(transfer.getAccountDestiny(), transfer.getValue());
			Map<String, String> header = new HashMap<String, String>();
			header.put("Content-Type", "application/json");

			try {

				request = new RequestHttp(HttpMethods.POST.getMethod(), "/account/deposit",HttpVersion.HTTP_1_1.toString(), header, deposit.toJSON());
				response = Http.sendHTTPRequestAndGetHttpResponse(request,transfer.getAccountDestiny().getBank().getIp());

			} catch (IOException e) {

				throw new ServerConnectionException();

			}
			if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

				accountOrigin.setBalance(accountOrigin.getBalance() - transfer.getValue());

				return Optional.of(accountOrigin);

			} else {

				return Optional.empty();

			}

		}

	}

	/**
	 * Autentica uma conta com base no ID e senha fornecidos.
	 * 
	 * @param account O objeto LoginAccountModel representando a conta a ser
	 *                autenticada.
	 * @return true se a autenticação for bem-sucedida, false caso contrário.
	 */
	public boolean authenticate(LoginAccountModel account) {

		for (Map.Entry<String, AccountModel> entry : accounts.entrySet()) {

			if (entry.getKey().equals(account.getId())&& entry.getValue().getPassword().equals(account.getPassword())) {

				
				return true;

			}

		}

		return false;

	}

	/**
	 * Procura uma conta pelo seu ID.
	 * 
	 * @param id O ID da conta.
	 * @return Um Optional contendo a conta encontrada, ou Optional.empty() caso a
	 *         conta não seja encontrada.
	 */
	public Optional<AccountModel> findById(String id) {

		for (Map.Entry<String, AccountModel> entry : accounts.entrySet()) {

			if (entry.getKey().equals(id)) {

				return Optional.of(entry.getValue());

			}

		}

		return Optional.empty();

	}

}
