package com.ynov.msaccount.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="t_account")
public class Account {
  @Id
  @GeneratedValue
  private Long id;
  private String libelle;
  private Double solde;
  private Long clientId;

  /**
   * Map un {@link AccountDto} dans un {@link Account}
   * @param accountDto {@link AccountDto}
   */
  public Account(AccountDto accountDto) {
    this.id = accountDto.getId();
    this.libelle = accountDto.getLibelle();
    this.solde = accountDto.getSolde();
    this.clientId = accountDto.getClientId();
  }
}
