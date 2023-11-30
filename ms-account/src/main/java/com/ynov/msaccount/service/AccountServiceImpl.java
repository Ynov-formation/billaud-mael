package com.ynov.msaccount.service;

import com.ynov.msaccount.exception.AccountAlreadyExists;
import com.ynov.msaccount.exception.ClientNotExists;
import com.ynov.msaccount.model.Account;
import com.ynov.msaccount.model.AccountDto;
import com.ynov.msaccount.model.ClientDto;
import com.ynov.msaccount.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final String clientServiceUrl = "http://localhost:8888/client/v1";

  public AccountServiceImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public AccountDto create(AccountDto account, Long clientId, String clientEmail) {
    RestTemplate restTemplate = new RestTemplate();

    // Appels API au ms_client pour vérifier que le client existe depuis son id ou son email
    if (clientId != null) {
      ClientNotExists clientNotExists = new ClientNotExists(account, true);
      try {
        Optional<ClientDto> client =
            Optional.ofNullable(restTemplate.getForObject(clientServiceUrl + "/{id}", ClientDto.class, clientId));
        if (client.isEmpty()) {
          return Optional.of(clientNotExists);
        }
        account.setClientId(clientId);
      } catch (HttpClientErrorException e) {
        return Optional.of(clientNotExists);
      }
    }

    if (clientEmail != null) {
      ClientNotExists clientNotExists = new ClientNotExists(account, false);
      try {
        Optional<ClientDto> client = Optional.ofNullable(
            restTemplate.getForObject(clientServiceUrl + "/email/{email}", ClientDto.class, clientEmail));
        if (client.isEmpty()) {
          return Optional.of(clientNotExists);
        }
        account.setClientId(client.get().getId());
      } catch (HttpClientErrorException e) {
        return Optional.of(clientNotExists);
      }
    }

    // Appel au repo pour vérifier que le compte n'existe pas déjà (clé clientId + libelle de compte)
    List<AccountDto> accounts = findByClientId(account.getClientId());
    boolean accountAlreadyExists = accounts.parallelStream().anyMatch(a -> a.getLibelle().equals(account.getLibelle()));
    if (accountAlreadyExists) {
      return Optional.of(new AccountAlreadyExists(account));
    }

    Account accountToSave = new Account(account);
    try {
      return Optional.of(new AccountDto(accountRepository.save(accountToSave)));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public AccountDto findById(Long id) {
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
