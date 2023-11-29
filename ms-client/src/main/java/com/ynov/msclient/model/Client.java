package com.ynov.msclient.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="t_client")
public class Client {
  @Id
  @GeneratedValue
  private Long id;
  private String nom;
  private String prenom;
  private String email;
  private LocalDate dateNaissance;
  private Long accountId;

  /**
   * Map a ClientDto to a Client
   * @param clientDto {@link ClientDto}
   */
  public Client(ClientDto clientDto) {
    this.id = clientDto.getId();
    this.nom = clientDto.getNom();
    this.prenom = clientDto.getPrenom();
    this.email = clientDto.getEmail();
    this.dateNaissance = clientDto.getDateNaissance();
    this.accountId = clientDto.getAccountId();
  }
}
