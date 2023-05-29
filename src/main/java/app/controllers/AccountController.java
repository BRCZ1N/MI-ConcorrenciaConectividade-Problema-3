package app.controllers;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.exceptions.InsufficientBalanceException;
import app.model.AccountModel;
import app.model.DepositModel;
import app.model.MessageModel;
import app.model.TransferModel;
import app.services.AccountServices;

@RestController
@RequestMapping("/account")
@ComponentScan("app.services")
public class AccountController {

	@Autowired
	private AccountServices service;
	private MessageModel message;

	@GetMapping("/auth")
	public ResponseEntity<String> authAccount(@RequestBody AccountModel account) {

		Optional<AccountModel> resultOptional = service.findByIdAndPassword(account);
		if (resultOptional.isEmpty()) {

			message.setMessage("Erro!! - Conta não encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());

		}

		message.setMessage("Sucesso!! - Conta autenticada");
//		return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());
		return ResponseEntity.status(HttpStatus.OK).body(new JSONObject(resultOptional.get()).toString());

	}
	
	@PostMapping("/createUser")
	public ResponseEntity<String> createAccount(@RequestBody AccountModel account) {

		Optional<AccountModel> resultOptional = service.createAccount(account);
		if (resultOptional.isEmpty()) {

			message.setMessage("Erro!! - Conta já cadastrada");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(message.toJSON());

		}

		message.setMessage("Sucesso!! - Conta criada");
		return ResponseEntity.status(HttpStatus.CREATED).body(message.toJSON());

	}
	
	@GetMapping("/balance")
	public ResponseEntity<String> getBalance(@RequestParam String id) {

		Optional<AccountModel> resultOptional = service.getBalanceOperation(id);
		if (resultOptional.isEmpty()) {

			message.setMessage("Erro!! - Conta não encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());

		}
		
		message.setMessage("Sucesso!! - O seu saldo é: "+ resultOptional.get().getBalance()+" R$");
		return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());

	}


	@PutMapping("/deposit")
	public ResponseEntity<String> makeDeposit(@RequestBody DepositModel deposit) {

		Optional<AccountModel> resultOptional = service.depositOperation(deposit);
		if (resultOptional.isEmpty()) {

			message.setMessage("Erro!! - Conta não encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());

		}
		
		message.setMessage("Sucesso!! - Deposito efetuado");
		return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());

	}
	
	@PutMapping("/transfer")
	public ResponseEntity<String> makeTransfer(@RequestBody TransferModel transfer) {

		try {

			Optional<AccountModel> resultOptional = service.transferOperation(transfer);
			
			if(resultOptional.isEmpty()) {
				
				message.setMessage("Erro!! - Conta não encontrada");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message.toJSON());
				
			}
			
			message.setMessage("Sucesso!! - Transferencia efetuada");
			return ResponseEntity.status(HttpStatus.OK).body(message.toJSON());

		} catch (InsufficientBalanceException e) {

			message.setMessage("Erro!! - Saldo insuficiente");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.toJSON());

		}

	}
	
}
