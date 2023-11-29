package com.ynov.msaccount.service;

import com.ynov.msaccount.model.Account;
import com.ynov.msaccount.model.AccountDto;
import com.ynov.msaccount.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;

  public AccountServiceImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Optional<AccountDto> create(AccountDto account, Long clientId, String clientEmail) {
    // Appels API au ms_client pour vérifier que le client existe depuis son id ou son email

    // Appel au repo pour vérifier que le compte n'existe pas déjà (clé clientId + libelle de compte)
    Account accountToSave = new Account(account);
    try {
      return Optional.of(new AccountDto(accountRepository.save(accountToSave)));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<AccountDto> findById(Long id) {
    try {
      Optional<Account> result = accountRepository.findById(id);
      return result.map(AccountDto::new);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public List<AccountDto> findByClientId(Long clientId) {
    List<Account> resultAccount;
    try {
      resultAccount = accountRepository.findByClientId(clientId);
    } catch (Exception e) {
      return Collections.emptyList();
    }
    return resultAccount.stream().map(AccountDto::new).toList();
  }

  @Override
  public List<AccountDto> findAll() {
    List<Account> resultAccount;
    try {
      resultAccount = accountRepository.findAll();
    } catch (Exception e) {
      return Collections.emptyList();
    }
    return resultAccount.stream().map(AccountDto::new).toList();
  }
}
