package com.ynov.msclient.repository;

import com.ynov.msclient.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Long>{
  /**
   * Recherche d'un client par son email
   * @param email email du client
   * @return le {@link Client} trouvé ou null si le client n'existe pas
   */
  @Query("SELECT c FROM Client c WHERE c.email = ?1")
    Client findByEmail(String email);
}
