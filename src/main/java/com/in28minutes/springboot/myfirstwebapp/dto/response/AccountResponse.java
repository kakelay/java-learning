package com.in28minutes.springboot.myfirstwebapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.in28minutes.springboot.myfirstwebapp.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {

    private Long id;

    private String accountNo;

    private String accountName;

    private String accountStatus;

    private String accountType;

    private String accountCode;

    private String currency;

    private BigDecimal balance;

    private String status;

    private BigDecimal availableBalance;

    private BigDecimal ledgerBalance;


    private AccountResponse toResponse(Account account){

        return AccountResponse.builder()
                .id(account.getId())
                .accountNo(account.getAccountNo())
                .accountName(account.getAccountName())
                .accountStatus(account.getStatus().name())
                .accountType(account.getAccountType())
                .accountCode(account.getProductCode())
                .currency(account.getCurrency())
                .availableBalance(account.getAvailableBalance())
                .ledgerBalance(account.getLedgerBalance())
                .build();
    }

}