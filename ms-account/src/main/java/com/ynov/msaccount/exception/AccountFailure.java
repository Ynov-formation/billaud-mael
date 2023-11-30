package com.ynov.msaccount.exception;

import com.ynov.msaccount.model.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountFailure extends AccountDto {
  private FailureEnum exceptionType;
}
