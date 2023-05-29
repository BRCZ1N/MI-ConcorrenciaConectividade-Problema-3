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
import org.springframework.web.bind.annotation.RestController;

import app.exceptions.InsufficientBalanceException;
import app.model.AccountModel;
import app.model.BalanceModel;
import app.model.DepositModel;
import app.model.TransferModel;
import app.services.AccountServices;

@RestController
@RequestMapping("/account")
@ComponentScan("app.services")
public class AccountController {

	@Autowired
	private AccountServices service;

	@GetMapping("/auth")
	public ResponseEntity<String> authAccount(@RequestBody AccountModel account) {

		Optional<AccountModel> resultOptional = service.findByIdAndPassword(account);
		if (resultOptional.isEmpty()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");

		}

		return ResponseEntity.status(HttpStatus.OK).body(new JSONObject(resultOptional.get()).toString());

	}
	
	@PostMapping("/createUser")
	public ResponseEntity<String> createAccount(@RequestBody AccountModel account) {

		Optional<AccountModel> resultOptional = service.createAccount(account);
		if (resultOptional.isEmpty()) {

			return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario já foi cadastrado");

		}

		return ResponseEntity.status(HttpStatus.CREATED).body("Sucesso");

	}
	
	@GetMapping("/balance")
	public ResponseEntity<String> getBalance(@RequestBody BalanceModel balance) {

		Optional<AccountModel> resultOptional = service.getBalanceOperation(balance);
		if (resultOptional.isEmpty()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");

		}

		return ResponseEntity.status(HttpStatus.OK).body(resultOptional.get().getBalance().toString());

	}


	@PutMapping("/deposit")
	public ResponseEntity<String> makeDeposit(@RequestBody DepositModel deposit) {

		Optional<AccountModel> resultOptional = service.depositOperation(deposit);
		if (resultOptional.isEmpty()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");

		}

		return ResponseEntity.status(HttpStatus.OK).body("Operação realizada com sucesso");

	}
	
	@PutMapping("/transfer")
	public ResponseEntity<String> makeTransfer(@RequestBody TransferModel transfer) {

		try {

			Optional<AccountModel> resultOptional = service.transferOperation(transfer);
			
			if(resultOptional.isEmpty()) {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client não encontrado");
				
			}
			
			return ResponseEntity.status(HttpStatus.OK).body("Operação realizada com sucesso");

		} catch (InsufficientBalanceException e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente");

		}

	}
	
}
