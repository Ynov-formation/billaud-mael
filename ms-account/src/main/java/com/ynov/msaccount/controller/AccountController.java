package com.ynov.msaccount.controller;

import com.ynov.msaccount.exception.AccountFailure;
import com.ynov.msaccount.exception.FailureEnum;
import com.ynov.msaccount.model.AccountDto;
import com.ynov.msaccount.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/v1")
@CrossOrigin("*")
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping
  public ResponseEntity<Object> create(@RequestBody AccountDto accountDto,
      @RequestParam(required = false, value = "client_id") Long clientId,
      @RequestParam(required = false, value = "client_email") String clientEmail) {
    if (clientId == null && clientEmail == null) {
      return ResponseEntity.badRequest().body("L'identifiant ou le mail du client doit être renseigné");
    }

    AccountDto createdAccount = accountService.create(accountDto, clientId, clientEmail);

    if (createdAccount instanceof AccountFailure accountFailure) {
      return checkAccountFailure(accountFailure);
    }

    return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody AccountDto accountDto) {
    AccountDto updatedAccount = accountService.update(id, accountDto);

    if (updatedAccount instanceof AccountFailure clientFailure) {
      return checkAccountFailure(clientFailure);
    }

    return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
  }

  // Utilisation d'un @PutMapping et non un @PatchMapping car
  // le PATCH est mal supporté par le RestTemplate
  @PutMapping("/solde/{id}")
  public ResponseEntity<Object> updateSolde(@PathVariable Long id, @RequestParam Double solde) {
    AccountDto updatedAccount = accountService.updateSolde(id, solde);

    if (updatedAccount instanceof AccountFailure clientFailure) {
      return checkAccountFailure(clientFailure);
    }

    return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable Long id) {
    AccountDto deletedClient = accountService.delete(id);

    if (deletedClient instanceof AccountFailure clientFailure) {
      return checkAccountFailure(clientFailure);
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/client/{id}")
  public ResponseEntity<Object> deleteByClientId(@PathVariable Long id) {
    List<Long> deletedAccounts = accountService.deleteByClientId(id);

    if (deletedAccounts.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(deletedAccounts, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findById(@PathVariable Long id) {
    AccountDto account = accountService.findById(id);

    if (account instanceof AccountFailure accountFailure) {
      return checkAccountFailure(accountFailure);
    }

    return new ResponseEntity<>(account, HttpStatus.OK);
  }

  @GetMapping("/client/{id}")
  public ResponseEntity<List<AccountDto>> findByClientId(@PathVariable Long id) {
    return ResponseEntity.ok(accountService.findByClientId(id));
  }

  @GetMapping
  public ResponseEntity<List<AccountDto>> findAll() {
    return ResponseEntity.ok(accountService.findAll());
  }

  /**
   * Vérifie le type d'exception et retourne la {@link ResponseEntity} correspondante
   *
   * @param accountFailure {@link AccountFailure}
   *
   * @return {@link ResponseEntity}
   */
  private ResponseEntity<Object> checkAccountFailure(AccountFailure accountFailure) {
    return switch (accountFailure.getExceptionType()) {
      case ACCOUNT_NOT_EXISTS ->
          new ResponseEntity<>(FailureEnum.ACCOUNT_NOT_EXISTS.getMessage(), HttpStatus.NOT_FOUND);
      case ACCOUNT_ALREADY_EXISTS ->
          new ResponseEntity<>(FailureEnum.ACCOUNT_ALREADY_EXISTS.getMessage(), HttpStatus.CONFLICT);
      case CAN_NOT_GET_CLIENT ->
          new ResponseEntity<>(FailureEnum.CAN_NOT_GET_CLIENT.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      case CLIENT_NOT_EXISTS -> new ResponseEntity<>(FailureEnum.CLIENT_NOT_EXISTS.getMessage(), HttpStatus.NOT_FOUND);
      case DATABASE -> new ResponseEntity<>(FailureEnum.DATABASE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    };
  }
}
