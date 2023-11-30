package com.ynov.msaccount.service;

import com.ynov.msaccount.exception.AccountFailure;
import com.ynov.msaccount.exception.FailureEnum;
import com.ynov.msaccount.model.Account;
import com.ynov.msaccount.model.AccountDto;
import com.ynov.msaccount.model.ClientDto;
import com.ynov.msaccount.repository.AccountRepository;
import com.ynov.msaccount.util.Util;
import org.springframework.http.HttpStatus;
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

  private static final String CLIENT_SERVICE_URL = "http://localhost:8888/client/v1";
  private final AccountRepository accountRepository;
  private final RestTemplate restTemplate = new RestTemplate();


  public AccountServiceImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public AccountDto create(AccountDto account, Long clientId, String clientEmail) {


    // Appels API au ms_client pour vérifier que le client existe depuis son id ou son email
    if (clientId != null) {
      AccountDto client = findClientByClientId(clientId);
      if (client instanceof AccountFailure) {
        return client;
      }
      account.setClientId(client.getClientId());
    }

    if (clientEmail != null) {
      AccountDto client = findClientByClientEmail(clientEmail);
      if (client instanceof AccountFailure) {
        return client;
      }
      account.setClientId(client.getClientId());
    }

    if (isAccountExisting(account).isPresent()) {
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
  public AccountDto update(Long id, AccountDto account) {
    try {
      // Compte stocké en base de donnée
      Account accountExisting = accountRepository.findById(id).orElse(null);
      if (accountExisting == null) {
        return new AccountFailure(FailureEnum.ACCOUNT_NOT_EXISTS);
      }

      account.setId(id);
      // Deep copie de accountExisting pour permettre de comparer les clientId
      Account accountToUpdate = new Account(accountExisting);
      Util.copyNonNullProperties(account, accountToUpdate);

      if (! accountExisting.getClientId().equals(accountToUpdate.getClientId())) {
        AccountDto client = findClientByClientId(accountToUpdate.getClientId());
        if (client instanceof AccountFailure) {
          return client;
        }

        // Si on veut affecter un compte à un client, mais que ce compte existe déjà,
        // on va additionner les soldes des deux comptes
        Optional<AccountDto> duplicatedAccount = isAccountExisting(new AccountDto(accountToUpdate));
        if (duplicatedAccount.isPresent()) {
          accountToUpdate.setSolde(accountToUpdate.getSolde() + duplicatedAccount.get().getSolde());
          return new AccountDto(accountRepository.save(accountToUpdate));
        }
      }
      return new AccountDto(accountRepository.save(accountToUpdate));
    } catch (Exception e) {
      return new AccountFailure(FailureEnum.DATABASE);
    }
  }

  @Override
  public AccountDto delete(Long id) {
    try {
      if (! accountRepository.existsById(id)) {
        return new AccountFailure(FailureEnum.ACCOUNT_NOT_EXISTS);
      }
      accountRepository.deleteById(id);
      return new AccountDto();
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

  /**
   * Appel au ms_client pour récupérer un client par son id
   *
   * @param clientId id du client
   *
   * @return {@link AccountDto} si le client a été trouvé, {@link AccountFailure} sinon
   */
  private AccountDto findClientByClientId(Long clientId) {
    try {
      Optional<ClientDto> client =
          Optional.ofNullable(restTemplate.getForObject(CLIENT_SERVICE_URL + "/{id}", ClientDto.class, clientId));
      if (client.isEmpty()) {
        return new AccountFailure(FailureEnum.CLIENT_NOT_EXISTS);
      }
      return AccountDto.builder().clientId(client.get().getId()).build();
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        return new AccountFailure(FailureEnum.CLIENT_NOT_EXISTS);
      }
      return new AccountFailure(FailureEnum.CAN_NOT_GET_CLIENT);
    }
  }

  /**
   * Appel au ms_client pour récupérer un client par son email
   *
   * @param clientEmail email du client
   *
   * @return {@link AccountDto} si le client a été trouvé, {@link AccountFailure} sinon
   */
  private AccountDto findClientByClientEmail(String clientEmail) {
    try {
      Optional<ClientDto> client = Optional.ofNullable(
          restTemplate.getForObject(CLIENT_SERVICE_URL + "/email/{email}", ClientDto.class, clientEmail));
      if (client.isEmpty()) {
        return new AccountFailure(FailureEnum.CLIENT_NOT_EXISTS);
      }
      return AccountDto.builder().clientId(client.get().getId()).build();
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        return new AccountFailure(FailureEnum.CLIENT_NOT_EXISTS);
      }
      return new AccountFailure(FailureEnum.CAN_NOT_GET_CLIENT);
    }
  }

  /**
   * Vérifie si un compte existe déjà pour un client (clé clientId + libelle de compte)
   *
   * @param account compte à vérifier
   *
   * @return le {@link AccountDto} si le compte existe déjà, {@link Optional#empty()} sinon
   */
  private Optional<AccountDto> isAccountExisting(AccountDto account) {
    List<AccountDto> accounts = findByClientId(account.getClientId());
    return accounts.parallelStream().filter(a -> a.getLibelle().equals(account.getLibelle())).findFirst();
  }
}
