package com.ynov.msaccount.service;

import com.ynov.msaccount.exception.AccountFailure;
import com.ynov.msaccount.model.AccountDto;

import java.util.List;

public interface AccountService {
  /**
   * Création d'un compte bancaire
   *
   * @param account {@link AccountDto}
   * @param clientId id du client
   * @param clientEmail email du client
   *
   * @return le {@link AccountDto} crée ou Optional.empty()
   */
  AccountDto create(AccountDto account, Long clientId, String clientEmail);

  /**
   * Mise à jour d'un account<br/>
   * Attention, si le client est modifié
   * <ul>
   *   <li>Vérification de l'existence du client</li>
   *   <li>Vérification de l'unicité du compte bancaire</li>
   *   <ul>
   *     <li>Si le compte bancaire existe déjà, fusion des deux comptes avec ajout des soldes</li>
   *     <li>Sinon, modification du compte</li>
   *   </ul>
   * </ul>
   *
   * @param id id du compte bancaire
   * @param account {@link AccountDto}
   *
   * @return le {@link AccountDto} mis à jour
   */
  AccountDto update(Long id, AccountDto account);

  /**
   * Suppression d'un compte bancaire
   *
   * @param id id du compte bancaire
   *
   * @return un {@link AccountDto} vide, un {@link AccountFailure} sinon
   */
  AccountDto delete(Long id);

  /**
   * Suppression de tous les comptes bancaires d'un client
   *
   * @param id id du client
   *
   * @return la liste des identifiants des {@link AccountDto} supprimés
   */
  List<Long> deleteByClientId(Long id);

  /**
   * Recherche d'un compte bancaire par son id
   *
   * @param id id du compte bancaire
   *
   * @return le {@link AccountDto} trouvé ou Optional.empty() si le compte bancaire n'existe pas
   */
  AccountDto findById(Long id);

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
