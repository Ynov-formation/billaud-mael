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
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "fr_FR", timezone = "Europe/Paris")
  private LocalDateTime date;
  private Long accountId;
  private String accountLibelle;
  private String clientNom;
  private String clientPrenom;
}
