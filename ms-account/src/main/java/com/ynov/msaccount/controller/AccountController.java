package com.ynov.msaccount.controller;

import com.ynov.msaccount.exception.AccountAlreadyExists;
import com.ynov.msaccount.exception.ClientNotExists;
import com.ynov.msaccount.model.AccountDto;
import com.ynov.msaccount.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    Optional<AccountDto> account = accountService.create(accountDto, clientId, clientEmail);

    if (account.isEmpty()) {
      return ResponseEntity.badRequest().body("Erreur lors de la création");
    }

    if (account.get() instanceof AccountAlreadyExists) {
      return ResponseEntity.badRequest().body("Le compte existe déjà");
    }

    if (account.get() instanceof ClientNotExists clientNotExists) {
      if (clientNotExists.isErrorFromClientId()) {
        return ResponseEntity.badRequest().body("L'identifiant du client n'est pas correcte.");
      }
      return ResponseEntity.badRequest().body("L'adresse email du client n'est pas correcte.");
    }

    return ResponseEntity.ok(account.get());
  }

  @GetMapping("/{id}")
  public ResponseEntity<AccountDto> findById(@PathVariable Long id) {
    return accountService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/client/{id}")
  public ResponseEntity<List<AccountDto>> findByClientId(@PathVariable Long id) {
    return ResponseEntity.ok(accountService.findByClientId(id));
  }

  @GetMapping
  public ResponseEntity<List<AccountDto>> findAll() {
    return ResponseEntity.ok(accountService.findAll());
  }
}
