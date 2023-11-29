package com.ynov.msclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
  private Long id;
  private String nom;
  private String prenom;
  private String email;
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate dateNaissance;
  private Long accountId;

  /**
   * Map a Client to a clientDto
   * @param client {@link Client}
   */
  public ClientDto(Client client) {
    this.id = client.getId();
    this.nom = client.getNom();
    this.prenom = client.getPrenom();
    this.email = client.getEmail();
    this.dateNaissance = client.getDateNaissance();
    this.accountId = client.getAccountId();
  }
}
