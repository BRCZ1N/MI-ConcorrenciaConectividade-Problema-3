package app.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import app.exceptions.InsufficientBalanceException;
import app.model.AccountModel;
import app.model.DepositModel;
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

		Optional<AccountModel> resultSearch = findById(deposit.getAccount().getId());

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

	public Optional<AccountModel> transferOperation(TransferModel transfer)throws InsufficientBalanceException {

		Optional<AccountModel> optionalAccountOrigin = findById(transfer.getAccountOrigin().getId());
		Optional<AccountModel> optionalAccountDestiny = findById(transfer.getAccountDestiny().getId());
		AccountModel accountOrigin;
		AccountModel accountDestiny;
		
		if (optionalAccountOrigin.isEmpty()) {

			return optionalAccountOrigin;

		} else {

			if (optionalAccountOrigin.get().getBalance() < transfer.getValue()) {

				throw new InsufficientBalanceException();

			} else {
				
				if(transfer.getAccountOrigin().getBank().equals(transfer.getAccountDestiny().getBank())) {
					 
					 accountOrigin = optionalAccountOrigin.get();
					 accountDestiny = optionalAccountDestiny.get();
					 
					 accountOrigin.setBalance(accountOrigin.getBalance() - transfer.getValue());
					 accountDestiny.setBalance(accountDestiny.getBalance() + transfer.getValue());
					 
					 accounts.replace(accountOrigin.getId(), accountOrigin); 
					 accounts.replace(accountDestiny.getId(), accountDestiny); 
					
					 return Optional.of(accountOrigin);
					 
				}else {
					
					RequestHttp request;
					ResponseHttp response;
					DepositModel deposit = new DepositModel(transfer.getAccountDestiny(),transfer.getValue());
					JSONObject json = new JSONObject(deposit);
					Map<String, String> header = new HashMap<String, String>();
					header.put("Content-Type","application/json");
							
					try {
						
						request = new RequestHttp(HttpMethods.GET.getMethod(),"/account/deposit",HttpVersion.HTTP_1_1.toString(),header,json.toString());
						response = Http.sendHTTPRequestAndGetHttpResponse(request, transfer.getAccountDestiny().getBank().getIp());
						
					} catch (IOException e) {
						
						return Optional.empty();
						
					}
					if(response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {
						
						accountOrigin = optionalAccountOrigin.get();
						accountOrigin.setBalance(accountOrigin.getBalance() - transfer.getValue());
						
						return Optional.of(accountOrigin);
						
					}else {
						
						return Optional.empty();
						
					}
					
				}

			}

		}

	}

	public Optional<AccountModel> findByIdAndPassword(AccountModel client) {

		for (Map.Entry<String, AccountModel> entry : accounts.entrySet()) {

			if (entry.getKey().equals(client.getId()) && entry.getValue().getPassword().equals(client.getPassword())) {

				return Optional.of(entry.getValue());

			}

		}

		return Optional.empty();

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
