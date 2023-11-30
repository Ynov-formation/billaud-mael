package com.ynov.msaccount.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
  private Long id;
  private String libelle;
  private Double solde;
  private Long clientId;

  /**
   * Map un {@link Account} dans un {@link AccountDto}
   * @param account {@link Account}
   */
  public AccountDto(Account account) {
    this.id = account.getId();
    this.libelle = account.getLibelle();
    this.solde = account.getSolde();
    this.clientId = account.getClientId();
  }

  public AccountDto(AccountDto accountDto) {
    this.id = accountDto.getId();
    this.libelle = accountDto.getLibelle();
    this.solde = accountDto.getSolde();
    this.clientId = accountDto.getClientId();
  }
}
