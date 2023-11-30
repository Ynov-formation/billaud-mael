package com.ynov.msoperation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
  private Long id;
  private String nom;
  private String prenom;
  private String email;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "fr_FR", timezone = "Europe/Paris")
  private LocalDate dateNaissance;
}
