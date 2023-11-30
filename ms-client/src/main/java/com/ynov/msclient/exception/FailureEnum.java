package com.ynov.msclient.exception;

import lombok.Getter;

@Getter
public enum FailureEnum {
  DATABASE("Erreur lors de l'action en base de données"),
  CLIENT_NOT_EXISTS("Le client n'existe pas"),
  CLIENT_ALREADY_EXISTS("Le client existe déjà");

  private final String message;

  FailureEnum(String message) {
    this.message = message;
  }
}
