package com.ynov.msaccount.exception;

import com.ynov.msaccount.model.AccountDto;

public class AccountAlreadyExists extends AccountDto {
    public AccountAlreadyExists(AccountDto accountDto) {
        super(accountDto);
    }
}
