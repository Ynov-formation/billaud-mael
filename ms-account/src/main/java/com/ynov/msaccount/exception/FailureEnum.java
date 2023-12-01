package com.ynov.msaccount.exception;

import lombok.Getter;

@Getter
public enum FailureEnum {
  ACCOUNT_NOT_EXISTS("Le compte bancaire n'existe pas"),
  ACCOUNT_ALREADY_EXISTS("Le compte bancaire existe déjà"),
  CAN_NOT_GET_CLIENT("Impossible de récupérer le client"),
  CLIENT_NOT_EXISTS("Le client n'existe pas"),
  DATABASE("Erreur lors de l'action en base de données");

  private final String message;

  FailureEnum(String message) {
    this.message = message;
  }
}
