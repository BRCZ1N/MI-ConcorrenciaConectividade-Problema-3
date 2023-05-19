package app.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import app.exceptions.InsufficientBalanceException;
import app.model.DepositModel;
import app.model.TransferModel;
import app.model.AccountModel;
import app.services.AccountServices;

@RestController
@RequestMapping("/account")
public class AccountController {

	private AccountServices service;

	@PostMapping("/auth")
	public ResponseEntity<String> authAccount(@RequestBody AccountModel user) {

		Optional<AccountModel> resultOptional = service.searchAccount(user);
		if (resultOptional.isEmpty()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");

		}

		return ResponseEntity.status(HttpStatus.OK).body("Sucesso");

	}
	
	@PostMapping("/createUser")
	public ResponseEntity<String> createUser(@RequestBody AccountModel user) {

		Optional<AccountModel> resultOptional = service.createAccount(user);
		if (resultOptional.isEmpty()) {

			return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario já foi cadastrado");

		}

		return ResponseEntity.status(HttpStatus.CREATED).body("Sucesso");

	}

	@PutMapping("/deposit")
	public ResponseEntity<String> deposit(@RequestBody DepositModel deposit) {

		Optional<AccountModel> resultOptional = service.depositOperation(deposit);
		if (resultOptional.isEmpty()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");

		}

		return ResponseEntity.status(HttpStatus.OK).body("Operação realizada com sucesso");

	}

	@PutMapping("/transfer")
	public ResponseEntity<String> transfer(@RequestBody TransferModel transfer) {

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
