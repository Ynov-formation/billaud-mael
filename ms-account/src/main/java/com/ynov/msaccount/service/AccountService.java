package com.ynov.msaccount.service;

import com.ynov.msaccount.model.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {
  /**
   * Création d'un compte bancaire
   *
   * @param account     {@link AccountDto}
   * @param clientId    id du client
   * @param clientEmail email du client
   *
   * @return le {@link AccountDto} crée ou Optional.empty()
   */
  Optional<AccountDto> create(AccountDto account, Long clientId, String clientEmail);

  /**
   * Recherche d'un compte bancaire par son id
   *
   * @param id id du compte bancaire
   *
   * @return le {@link AccountDto} trouvé ou Optional.empty() si le compte bancaire n'existe pas
   */
  Optional<AccountDto> findById(Long id);

  /**
   * Récupération de tous les comptes bancaires d'un client
   *
   * @param clientId id du client
   *
   * @return la liste des {@link AccountDto} ou une liste vide si le client n'a pas de compte bancaire
   */
  List<AccountDto> findByClientId(Long clientId);

  /**
   * Récupération de tous les comptes bancaires
   *
   * @return la liste des {@link AccountDto} ou une liste vide si aucun compte bancaire n'existe
   */
  List<AccountDto> findAll();
}
