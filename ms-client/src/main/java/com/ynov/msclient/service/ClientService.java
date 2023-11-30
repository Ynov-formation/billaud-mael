package com.ynov.msclient.service;

import com.ynov.msclient.model.ClientDto;

import java.util.List;
import java.util.Optional;

public interface ClientService {
  /**
   * Création d'un client
   *
   * @param client {@link ClientDto}
   *
   * @return le {@link ClientDto} crée ou Optional.empty() si le client existe déjà
   */
  Optional<ClientDto> create(ClientDto client);

  /**
   * Mise à jour d'un client
   *
   * @param id id du client
   * @param client {@link ClientDto}
   *
   * @return le {@link ClientDto} mis à jour ou Optional.empty() si le client n'existe pas
   */
  Optional<ClientDto> update(Long id, ClientDto client);

  /**
   * Suppression d'un client
   *
   * @param id id du client
   *
   * @return true si le client a été supprimé, false sinon
   */
  boolean delete(Long id);

  /**
   * Recherche d'un client par son id
   *
   * @param id id du client
   *
   * @return le {@link ClientDto} trouvé ou Optional.empty() si le client n'existe pas
   */
  Optional<ClientDto> findById(Long id);

  /**
   * Recherche d'un client par son email
   *
   * @param email email du client
   *
   * @return le {@link ClientDto} trouvé ou Optional.empty() si le client n'existe pas
   */
  Optional<ClientDto> findByEmail(String email);

  /**
   * Récupération de tous les clients
   *
   * @return la liste des {@link ClientDto} ou une liste vide si aucun client n'existe
   */
  List<ClientDto> findAll();
}
