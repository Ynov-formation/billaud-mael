package com.ynov.msaccount.service;

import com.ynov.msaccount.exception.AccountFailure;
import com.ynov.msaccount.exception.FailureEnum;
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

  public AccountServiceImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public AccountDto create(AccountDto account, Long clientId, String clientEmail) {
    final String clientServiceUrl = "http://localhost:8888/client/v1";
    RestTemplate restTemplate = new RestTemplate();

    // Appels API au ms_client pour vérifier que le client existe depuis son id ou son email
    if (clientId != null) {
      try {
        Optional<ClientDto> client =
            Optional.ofNullable(restTemplate.getForObject(clientServiceUrl + "/{id}", ClientDto.class, clientId));
        if (client.isEmpty()) {
          return new AccountFailure(FailureEnum.CLIENT_NOT_EXISTS);
        }
        account.setClientId(clientId);
      } catch (HttpClientErrorException e) {
        return new AccountFailure(FailureEnum.CAN_NOT_GET_CLIENT);
      }
    }

    if (clientEmail != null) {
      try {
        Optional<ClientDto> client = Optional.ofNullable(
            restTemplate.getForObject(clientServiceUrl + "/email/{email}", ClientDto.class, clientEmail));
        if (client.isEmpty()) {
          return new AccountFailure(FailureEnum.CLIENT_NOT_EXISTS);
        }
        account.setClientId(client.get().getId());
      } catch (HttpClientErrorException e) {
        return new AccountFailure(FailureEnum.CAN_NOT_GET_CLIENT);
      }
    }

    // Appel au repo pour vérifier que le compte n'existe pas déjà (clé clientId + libelle de compte)
    List<AccountDto> accounts = findByClientId(account.getClientId());
    boolean accountAlreadyExists = accounts.parallelStream().anyMatch(a -> a.getLibelle().equals(account.getLibelle()));
    if (accountAlreadyExists) {
      return new AccountFailure(FailureEnum.ACCOUNT_ALREADY_EXISTS);
    }

    Account accountToSave = new Account(account);
    try {
      return new AccountDto(accountRepository.save(accountToSave));
    } catch (Exception e) {
      return new AccountFailure(FailureEnum.DATABASE);
    }
  }

  @Override
  public AccountDto findById(Long id) {
    try {
      Optional<Account> result = accountRepository.findById(id);
      return result.map(AccountDto::new).orElse(new AccountFailure(FailureEnum.ACCOUNT_NOT_EXISTS));
    } catch (Exception e) {
      return new AccountFailure(FailureEnum.DATABASE);
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
