package com.ynov.msoperation.exception;

import com.ynov.msoperation.model.OperationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OperationFailure extends OperationDto {
  private FailureEnum exceptionType;
}
