package com.ynov.msoperation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "t_client")
public class Operation {
  @Id
  @GeneratedValue
  private Long id;
  private OperationTypeEnum type;
  private Double montant;
  private LocalDateTime date;
  private Long accountId;
}
