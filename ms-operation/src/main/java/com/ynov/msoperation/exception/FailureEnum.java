package com.ynov.msoperation.exception;

import lombok.Getter;

@Getter
public enum FailureEnum {
  CAN_NOT_GET_ACCOUNT("Impossible de récupérer le compte bancaire"),
  ACCOUNT_NOT_EXISTS("Le compte bancaire n'existe pas"),
  INSUFFICIENT_ACCOUNT_BALANCE("Solde insuffisant pour le débit"),
  CAN_NOT_GET_CLIENT("Impossible de récupérer le client"),
  CLIENT_NOT_EXISTS("Le client n'existe pas"),
  DATABASE("Erreur lors de l'action en base de données");

  private final String message;

  FailureEnum(String message) {
    this.message = message;
  }
}
