package com.ynov.msoperation.service;

import com.ynov.msoperation.model.OperationDto;

import java.util.List;

public interface OperationService {
  /**
   * Création d'une opération de débit
   *
   * @param accountId id du compte bancaire à débiter
   * @param amount montant à débiter
   *
   * @return l'opération créée avec des informations complémentaires
   */
  OperationDto debit(Long accountId, Double amount);

  /**
   * Création d'une opération de crédit
   *
   * @param accountId id du compte bancaire à créditer
   * @param amount montant à créditer
   *
   * @return l'opération créée avec des informations complémentaires
   */
  OperationDto credit(Long accountId, Double amount);

  /**
   * Création d'une opération de virement (débit d'un compte et crédit d'un autre)
   *
   * @param accountToDebitId id du compte bancaire à débiter
   * @param accountToCreditId id du compte bancaire à créditer
   * @param amount montant de la transaction
   *
   * @return la liste des opérations créées avec des informations complémentaires
   */
  List<OperationDto> wireTransfert(Long accountToDebitId, Long accountToCreditId, Double amount);

  /**
   * Récupération de la liste des opérations d'un compte bancaire selon des filtres
   *
   * @param accountId id du compte bancaire
   * @param clientId id du client
   * @param type type d'opération (CREDIT | DEBIT)
   *
   * @return la liste des opérations filtrées
   */
  List<OperationDto> getOperationsByFilters(Long accountId, Long clientId, String type);
}
