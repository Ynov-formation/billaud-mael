package com.ynov.msoperation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_operation")
public class Operation {
  @Id
  @GeneratedValue
  private Long id;
  @Enumerated(EnumType.STRING)
  private OperationTypeEnum type;
  private Double montant;
  private Double soldeAtOperation;
  private LocalDateTime date;
  private Long accountId;

  /**
   * Map un {@link OperationDto} dans un {@link Operation}
   *
   * @param operationDto {@link OperationDto}
   */
  public Operation(OperationDto operationDto) {
    this.id = operationDto.getId();
    this.type = operationDto.getType();
    this.montant = operationDto.getMontant();
    this.soldeAtOperation = operationDto.getSoldeAtOperation();
    this.date = operationDto.getDate();
    this.accountId = operationDto.getAccountId();
  }
}
