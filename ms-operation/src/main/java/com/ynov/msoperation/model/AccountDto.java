package com.ynov.msoperation.model;

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
}
