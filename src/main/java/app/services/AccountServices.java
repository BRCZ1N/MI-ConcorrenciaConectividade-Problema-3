package app.services;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import app.exceptions.InsufficientBalanceException;
import app.model.AccountModel;
import io.netty.handler.codec.http.HttpResponse;
import app.model.DepositModel;
import app.model.TransferModel;

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

	public synchronized Optional<AccountModel> createAccount(AccountModel client) {
		
		if (accounts.containsValue(client)) {

			return Optional.empty();

		}

		client.setId(String.valueOf(id));
		accounts.put(String.valueOf(id), client);
		id += 1;

		return Optional.of(client);

	}
	

	public synchronized Optional<AccountModel> depositOperation(DepositModel deposit) {

		Optional<AccountModel> resultSearch = searchAccount(deposit.getAccount());

		if (resultSearch.isEmpty()) {

			return resultSearch;

		}

		AccountModel clientAccount = resultSearch.get();
		clientAccount.setBalance(clientAccount.getBalance() + deposit.getValue());
		accounts.replace(clientAccount.getId(), clientAccount);
		return Optional.of(clientAccount);

	}

	public synchronized Optional<AccountModel> transferOperation(TransferModel transfer)throws InsufficientBalanceException {

		Optional<AccountModel> optionalAccountOrigin = searchAccount(transfer.getAccountOrigin());
		Optional<AccountModel> optionalAccountDestiny = searchAccount(transfer.getAccountDestiny());
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
					
					//Envia requisição HTTP do banco atual para o proximo banco, essa será uma requisição de deposito
					//Ajuste do erro de conta inexistente também aqui.
					if(HttpResponse.getStatusLine().equals(HttpStatus.OK)) {
						
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

	public Optional<AccountModel> searchAccount(AccountModel client) {

		for (Map.Entry<String, AccountModel> entry : accounts.entrySet()) {

			if (entry.getKey().equals(client.getId()) && entry.getValue().getPassword().equals(client.getPassword())) {

				return Optional.of(entry.getValue());

			}

		}

		return Optional.empty();

	}

}
