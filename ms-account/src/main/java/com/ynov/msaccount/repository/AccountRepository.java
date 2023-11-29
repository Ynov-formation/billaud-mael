package com.ynov.msaccount.repository;

import com.ynov.msaccount.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
  /**
   * Recherche d'un compte par son client_id
   *
   * @param clientId id du client
   *
   * @return la liste des {@link Account} ou une liste vide si le client n'a pas de compte bancaire
   */
  @Query("SELECT a FROM Account a WHERE a.clientId = ?1")
  List<Account> findByClientId(Long clientId);
}
