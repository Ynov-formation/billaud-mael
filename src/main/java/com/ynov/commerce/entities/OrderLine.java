package com.ynov.commerce.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_commande_line")
@Builder
public class OrderLine {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private int quantity;
  private double pricePerUnit;
  private String description;
  @ManyToOne
  private Order order;
  @OneToOne
  private Product product;
}
