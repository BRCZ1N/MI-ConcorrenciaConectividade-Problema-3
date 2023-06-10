package app.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
import io.netty.handler.codec.http.HttpVersion;

@Component
public class AccountServices {

	private Map<String, AccountModel> accounts;
	private long id = 0;

	public AccountServices() {

		this.setAccounts(new ConcurrentHashMap<String, AccountModel>());

	}

	public Map<String, AccountModel> getAccounts() {
		return accounts;
	}

	public void setAccounts(Map<String, AccountModel> accounts) {
		this.accounts = accounts;
	}

	public Optional<AccountModel> createAccount(AccountModel client) {

		if (accounts.containsValue(client)) {

			return Optional.empty();

		}

		client.setId(String.valueOf(id));
		accounts.put(String.valueOf(id), client);
		id += 1;

		return Optional.of(client);

	}

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

	public Optional<AccountModel> getBalanceOperation(String id) {

		Optional<AccountModel> resultSearch = findById(id);

		if (resultSearch.isEmpty()) {

			return resultSearch;

		}

		return Optional.of(resultSearch.get());

	}

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

				request = new RequestHttp(HttpMethods.PUT.getMethod(), "/account/deposit",HttpVersion.HTTP_1_1.toString(), header, deposit.toJSON());
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

	public boolean authenticate(LoginAccountModel account) {

		for (Map.Entry<String, AccountModel> entry : accounts.entrySet()) {

			if (entry.getKey().equals(account.getId())&& entry.getValue().getPassword().equals(account.getPassword())) {

				
				return true;

			}

		}

		return false;

	}

	public Optional<AccountModel> findById(String id) {

		for (Map.Entry<String, AccountModel> entry : accounts.entrySet()) {

			if (entry.getKey().equals(id)) {

				return Optional.of(entry.getValue());

			}

		}

		return Optional.empty();

	}

}
