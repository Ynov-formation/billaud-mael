package com.ynov.msaccount.exception;

import com.ynov.msaccount.model.AccountDto;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClientNotExists extends AccountDto {
  private boolean errorFromClientId;

  public ClientNotExists(AccountDto accountDto, boolean errorFromClientId) {
    super(accountDto);
    this.errorFromClientId = errorFromClientId;
  }
}
