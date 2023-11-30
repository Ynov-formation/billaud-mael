package com.ynov.msclient.expection;

import com.ynov.msclient.model.ClientDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClientFailure extends ClientDto {
  private FailureEnum exceptionType;
}
