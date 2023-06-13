package app.controllers;

import java.net.UnknownHostException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.exceptions.InsufficientBalanceException;
import app.exceptions.ServerConnectionException;
import app.model.AccountModel;
import app.model.DepositModel;
import app.model.LoginAccountModel;
import app.model.MessageModel;
import app.model.RequestSynchronObject;
import app.model.TransferModel;
import app.services.AccountServices;
import app.services.SynchronizerServices;

@RestController
@RequestMapping("/account")
@ComponentScan("app.services")
public class AccountController {

	@Autowired
	private AccountServices serviceAccount;
	@Autowired
	private SynchronizerServices serviceSynch;
	private MessageModel message;

	public AccountController() {

		this.message = new MessageModel();

	}

	@PostMapping("/auth")
	public ResponseEntity<String> authAccount(@RequestBody LoginAccountModel login) {

		if (!serviceAccount.authenticate(login)) {

			message.setMessage("Erro!! - Conta não encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());

		}

		message.setMessage("Sucesso!! - Conta autenticada");
		return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());

	}

	@PostMapping("/create")
	public ResponseEntity<String> createAccount(@RequestBody AccountModel account) {

		Optional<AccountModel> resultOptional = serviceAccount.createAccount(account);
		if (resultOptional.isEmpty()) {

			message.setMessage("Erro!! - Conta já cadastrada");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(message.toJSON());

		}

		message.setMessage("Sucesso!! - Conta criada");
		return ResponseEntity.status(HttpStatus.CREATED).body(message.toJSON());

	}

	@GetMapping("/balance")
	public ResponseEntity<String> getBalance(@RequestParam("id") String id) {

		Optional<AccountModel> resultOptional = serviceAccount.getBalanceOperation(id);
		if (resultOptional.isEmpty()) {

			message.setMessage("Erro!! - Conta não encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());

		}

		message.setMessage("O seu saldo é: " + resultOptional.get().getBalance() + " R$");
		return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());

	}

	@PostMapping("/reply")
	public ResponseEntity<String> replyRequest(@RequestBody RequestSynchronObject synch) throws UnknownHostException {

		return ResponseEntity.status(HttpStatus.OK).body(serviceSynch.replyMessage(synch).toJSON());

	}

	@PostMapping("/deposit")
	public ResponseEntity<String> makeDeposit(@RequestBody DepositModel deposit) {

		try {
			
			serviceSynch.enterCriticalRegion(deposit);
			Optional<AccountModel> resultOptional = serviceAccount.depositOperation(deposit);
			if (resultOptional.isEmpty()) {

				message.setMessage("Erro!! - Conta não encontrada");
				serviceSynch.exitCriticalRegion(deposit);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());

			}

			message.setMessage("Sucesso!! - Deposito efetuado");
			serviceSynch.exitCriticalRegion(deposit);
			return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());
			
		} catch (ServerConnectionException e) {
			
			message.setMessage("Erro!! - " + e.getMessage());
			serviceSynch.exitCriticalRegion(deposit);
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(message.toJSON());
		}

	}

	@PostMapping("/transfer")
	public ResponseEntity<String> makeTransfer(@RequestBody TransferModel transfer) {

		try {

			serviceSynch.enterCriticalRegion(transfer);
			Optional<AccountModel> resultOptional = serviceAccount.transferOperation(transfer);

			if (resultOptional.isEmpty()) {

				message.setMessage("Erro!! - Conta não encontrada");
				serviceSynch.exitCriticalRegion(transfer);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());

			}

			message.setMessage("Sucesso!! - Transferencia efetuada");
			serviceSynch.exitCriticalRegion(transfer);
			return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());

		} catch (InsufficientBalanceException e) {

			message.setMessage("Erro!! - " + e.getMessage());
			serviceSynch.exitCriticalRegion(transfer);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.toJSON());

		} catch (ServerConnectionException e) {

			message.setMessage("Erro!! - " + e.getMessage());
			serviceSynch.exitCriticalRegion(transfer);
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(message.toJSON());
		}

	}

}
