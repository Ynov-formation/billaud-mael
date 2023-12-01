package com.ynov.msoperation.controller;

import com.ynov.msoperation.exception.FailureEnum;
import com.ynov.msoperation.exception.OperationFailure;
import com.ynov.msoperation.model.OperationDto;
import com.ynov.msoperation.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operation/v1")
@CrossOrigin("*")
public class OperationController {
  private final OperationService operationService;

  public OperationController(OperationService operationService) {
    this.operationService = operationService;
  }

  @PostMapping("/debit")
  public ResponseEntity<Object> debit(@RequestParam(value = "account_id") Long accountId, @RequestParam Double amount) {
    OperationDto operationDto = operationService.debit(accountId, amount);

    if (operationDto instanceof OperationFailure operationFailure) {
      return checkOperationFailure(operationFailure);
    }

    return new ResponseEntity<>(operationDto, HttpStatus.CREATED);
  }

  /**
   * Vérifie le type d'exception et retourne la {@link ResponseEntity} correspondante
   *
   * @param accountFailure {@link OperationFailure}
   *
   * @return {@link ResponseEntity}
   */
  private ResponseEntity<Object> checkOperationFailure(OperationFailure accountFailure) {
    return switch (accountFailure.getExceptionType()) {
      case CAN_NOT_GET_ACCOUNT -> new ResponseEntity<>(FailureEnum.CAN_NOT_GET_ACCOUNT.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR);
      case CAN_NOT_UPDATE_SOLDE -> new ResponseEntity<>(FailureEnum.CAN_NOT_UPDATE_SOLDE.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR);
      case ACCOUNT_NOT_EXISTS ->
          new ResponseEntity<>(FailureEnum.ACCOUNT_NOT_EXISTS.getMessage(), HttpStatus.NOT_FOUND);
      case INSUFFICIENT_ACCOUNT_BALANCE ->
          new ResponseEntity<>(FailureEnum.INSUFFICIENT_ACCOUNT_BALANCE.getMessage(), HttpStatus.CONFLICT);
      case CAN_NOT_GET_CLIENT ->
          new ResponseEntity<>(FailureEnum.CAN_NOT_GET_CLIENT.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      case CLIENT_NOT_EXISTS -> new ResponseEntity<>(FailureEnum.CLIENT_NOT_EXISTS.getMessage(), HttpStatus.NOT_FOUND);
      case DATABASE -> new ResponseEntity<>(FailureEnum.DATABASE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    };
  }
}
