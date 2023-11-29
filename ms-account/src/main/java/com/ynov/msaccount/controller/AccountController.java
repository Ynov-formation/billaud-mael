package com.ynov.msaccount.controller;

import com.ynov.msaccount.model.AccountDto;
import com.ynov.msaccount.service.AccountService;
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
    return accountService
        .create(accountDto, clientId, clientEmail)
        .<ResponseEntity<Object>>map(ResponseEntity::ok)
        .orElse(ResponseEntity.badRequest().body("Erreur lors de la création ou le client à déjà créée ce compte"));
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
