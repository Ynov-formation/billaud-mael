package com.ynov.msoperation.exception;

import lombok.Getter;

@Getter
public enum FailureEnum {
  OPERATION_TYPE_NOT_EXISTS("Le type d'opération n'existe pas, veillez choisir DEBIT ou CREDIT."),
  CAN_NOT_GET_ACCOUNT("Impossible de récupérer le compte bancaire"),
  CAN_NOT_UPDATE_SOLDE("Impossible de mettre à jour le solde du compte bancaire"),
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
