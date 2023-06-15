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

/**
 * Classe responsável pelo controller da aplicação do banco
 */
@RestController
@RequestMapping("/account")
@ComponentScan("app.services")
public class AccountController {

	@Autowired
	private AccountServices serviceAccount;
	@Autowired
	private SynchronizerServices serviceSynch;
	private MessageModel message;

	/**
	 * Construtor da classe AccountController.
	 * Inicializa a variável de mensagem.
	 */
	public AccountController() {

		this.message = new MessageModel();

	}

	/**
	 * Metodo e endpoint para autenticar uma conta.
	 *
	 * @param login O objeto LoginAccountModel contendo os dados de login.
	 * @return Uma resposta HTTP indicando o resultado da autenticação.
	 */
	@PostMapping("/auth")
	public ResponseEntity<String> authAccount(@RequestBody LoginAccountModel login) {

		if (!serviceAccount.authenticate(login)) {

			message.setMessage("Erro!! - Conta não encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());

		}

		message.setMessage("Sucesso!! - Conta autenticada");
		return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());

	}

	/**
	 * Endpoint para criar uma nova conta.
	 *
	 * @param account O objeto AccountModel contendo os dados da conta a ser criada.
	 * @return Uma resposta HTTP indicando o resultado da criação da conta.
	 */
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

	/**
	 * Metodo e endpoint para obter o saldo de uma conta.
	 *
	 * @param id O ID da conta.
	 * @return Uma resposta HTTP contendo o saldo da conta.
	 */
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

	/**
	 * Metodo e endpoint para responder a uma requisição de sincronização.
	 *
	 * @param synch O objeto RequestSynchronObject contendo os dados da requisição de sincronização.
	 * @return Uma resposta HTTP contendo a mensagem de resposta.
	 * @throws UnknownHostException se o endereço do host for desconhecido.
	 */
	@PostMapping("/reply")
	public ResponseEntity<String> replyRequest(@RequestBody RequestSynchronObject synch) throws UnknownHostException {

		return ResponseEntity.status(HttpStatus.OK).body(serviceSynch.replyMessage(synch).toJSON());

	}

	/**
	 * Metodo e endpoint para realizar um depósito em uma conta.
	 *
	 * @param deposit O objeto DepositModel contendo os dados do depósito.
	 * @return Uma resposta HTTP indicando o resultado do depósito.
	 */
	@PostMapping("/deposit")
	public ResponseEntity<String> makeDeposit(@RequestBody DepositModel deposit) {

		try {
			
			serviceSynch.requestMessage(deposit);
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

	/**
	 * Metodo e endpoint para realizar uma transferência entre contas.
	 *
	 * @param transfer O objeto TransferModel contendo os dados da transferência.
	 * @return Uma resposta HTTP indicando o resultado da transferência.
	 */
	@PostMapping("/transfer")
	public ResponseEntity<String> makeTransfer(@RequestBody TransferModel transfer) {

		try {

			serviceSynch.requestMessage(transfer);
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
