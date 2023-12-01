package com.ynov.msoperation.service;

import com.ynov.msoperation.exception.FailureEnum;
import com.ynov.msoperation.exception.OperationFailure;
import com.ynov.msoperation.model.*;
import com.ynov.msoperation.repository.OperationRepository;
import com.ynov.msoperation.util.Util;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OperationServiceImpl implements OperationService {

  private static final String GATEWAY_URL = "http://localhost:8888";
  private static final String MS_ACCOUNT_URL = GATEWAY_URL + "/account/v1";
  private static final String MS_CLIENT_URL = GATEWAY_URL + "/client/v1";
  private final OperationRepository operationRepository;
  private final RestTemplate restTemplate = new RestTemplate();

  public OperationServiceImpl(OperationRepository operationRepository) {
    this.operationRepository = operationRepository;
  }

  @Override
  public OperationDto debit(Long accountId, Double amount) {
    OperationDto operationInformations = getAccountAndClientInformationsByAccountId(accountId);
    if (operationInformations instanceof OperationFailure) {
      return operationInformations;
    }

    if (operationInformations.getAccountSolde() < amount) {
      return new OperationFailure(FailureEnum.INSUFFICIENT_ACCOUNT_BALANCE);
    }

    OperationDto soldeUpdated = setAccountSolde(operationInformations.getAccountId(),
        operationInformations.getAccountSolde() - amount);
    if (soldeUpdated instanceof OperationFailure) {
      return soldeUpdated;
    }

    operationInformations.setAccountSolde(soldeUpdated.getAccountSolde());
    operationInformations.setMontant(amount);
    operationInformations.setType(OperationTypeEnum.DEBIT);
    operationInformations.setDate(Util.getCurrentDate());

    try {
      operationInformations.setId(operationRepository.save(new Operation(operationInformations)).getId());
      return operationInformations;
    } catch (Exception e) {
      return new OperationFailure(FailureEnum.DATABASE);
    }
  }

  @Override
  public OperationDto credit(Long accountId, Double amount) {
    return null;
  }

  @Override
  public List<OperationDto> wireTransfert(Long accountToDebitId, Long accountToCreditId, Double amount) {
    return null;
  }

  /**
   * Appel au ms_client pour récupérer un client par son id
   *
   * @param clientId id du client
   *
   * @return {@link OperationDto} si le client a été trouvé, {@link OperationFailure} sinon
   */
  private OperationDto findClientInformationsByClientId(Long clientId) {
    try {
      ClientDto client = restTemplate.getForObject(MS_CLIENT_URL + "/{id}", ClientDto.class, clientId);
      if (client == null) {
        return new OperationFailure(FailureEnum.CLIENT_NOT_EXISTS);
      }
      return OperationDto
          .builder()
          .clientId(client.getId())
          .clientNom(client.getNom())
          .clientPrenom(client.getPrenom())
          .build();
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        return new OperationFailure(FailureEnum.CLIENT_NOT_EXISTS);
      }
      return new OperationFailure(FailureEnum.CAN_NOT_GET_CLIENT);
    }
  }

  /**
   * Appel au ms_account pour récupérer un account par son id
   *
   * @param accountId id du compte bancaire
   *
   * @return {@link OperationDto} si le client a été trouvé, {@link OperationFailure} sinon
   */
  private OperationDto findAccountInformationsByAccountId(Long accountId) {
    try {
      AccountDto account = restTemplate.getForObject(MS_ACCOUNT_URL + "/{id}", AccountDto.class, accountId);
      if (account == null) {
        return new OperationFailure(FailureEnum.ACCOUNT_NOT_EXISTS);
      }
      return OperationDto
          .builder()
          .accountId(account.getId())
          .accountLibelle(account.getLibelle())
          .accountSolde(account.getSolde())
          .clientId(account.getClientId())
          .build();
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        return new OperationFailure(FailureEnum.ACCOUNT_NOT_EXISTS);
      }
      return new OperationFailure(FailureEnum.CAN_NOT_GET_ACCOUNT);
    }
  }

  /**
   * Récupère les informations d'un compte bancaire et de son client
   *
   * @param accountId id du compte bancaire
   *
   * @return {@link OperationDto} si les informations ont été trouvées, {@link OperationFailure} sinon
   */
  private OperationDto getAccountAndClientInformationsByAccountId(Long accountId) {
    OperationDto informations = new OperationDto();
    OperationDto accountInformations = findAccountInformationsByAccountId(accountId);
    if (accountInformations instanceof OperationFailure) {
      return accountInformations;
    }

    OperationDto clientInformations = findClientInformationsByClientId(accountInformations.getClientId());
    if (clientInformations instanceof OperationFailure) {
      return clientInformations;
    }

    Util.copyNonNullProperties(accountInformations, informations);
    Util.copyNonNullProperties(clientInformations, informations);
    return informations;
  }

  /**
   * Met à jour le solde d'un compte bancaire
   *
   * @param accountId id du compte bancaire
   * @param solde nouveau solde du compte bancaire
   *
   * @return {@link OperationDto} vide si le solde a été mis à jour, {@link OperationFailure} sinon
   */
  private OperationDto setAccountSolde(Long accountId, Double solde) {
    String url = MS_ACCOUNT_URL + "/solde/{id}";
    Map<String, String> params = new HashMap<>();
    params.put("id", accountId.toString());
    URI uri = UriComponentsBuilder.fromUriString(url)
                                  .buildAndExpand(params)
                                  .toUri();
    uri = UriComponentsBuilder
        .fromUri(uri)
        .queryParam("solde", solde)
        .build()
        .toUri();
    try {
      restTemplate.exchange(uri, HttpMethod.PUT, null, Void.class);
      return OperationDto.builder().accountId(accountId).accountSolde(solde).build();
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        return new OperationFailure(FailureEnum.ACCOUNT_NOT_EXISTS);
      }
      return new OperationFailure(FailureEnum.CAN_NOT_UPDATE_SOLDE);
    }
  }
}
