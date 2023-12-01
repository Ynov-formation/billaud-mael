package com.ynov.msoperation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationDto {
  private Long id;
  private OperationTypeEnum type;
  private Double montant;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", locale = "fr_FR",
              timezone = "Europe/Paris")
  private LocalDateTime date;
  private Long accountId;
  private String accountLibelle;
  private Double accountSolde;
  private Long clientId;
  private String clientNom;
  private String clientPrenom;

  /**
   * Map un {@link Operation} dans un {@link OperationDto}
   *
   * @param operation {@link Operation}
   */
  public OperationDto(Operation operation) {
    this.id = operation.getId();
    this.type = operation.getType();
    this.montant = operation.getMontant();
    this.date = operation.getDate();
    this.accountId = operation.getAccountId();
  }
}