package com.in28minutes.springboot.myfirstwebapp.controller.user.account;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.common.RequestReference;
import com.in28minutes.springboot.myfirstwebapp.common.TraceLogger;
import com.in28minutes.springboot.myfirstwebapp.dto.response.AccountResponse;
import com.in28minutes.springboot.myfirstwebapp.entity.Account;
import com.in28minutes.springboot.myfirstwebapp.repository.AccountRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
        private final AccountRepository accountRepository;
        private final MessageSource messageSource;
        private final TraceLogger traceLogger;

        public AccountController(AccountRepository accountRepository,
                        MessageSource messageSource,
                        TraceLogger traceLogger) {
                this.accountRepository = accountRepository;
                this.messageSource = messageSource;
                this.traceLogger = traceLogger;
        }

        private String generateReference() {
                return RequestReference.getOrCreate();
        }

        private AccountResponse toResponse(Account account) {
                return AccountResponse.builder()
                                .id(account.getId())
                                .accountNo(account.getAccountNo())
                                .accountName(account.getAccountName())
                                .accountStatus(account.getStatus() != null ? account.getStatus().name() : null)
                                .accountType(account.getAccountType())
                                .accountCode(account.getProductCode())
                                .currency(account.getCurrency())
                                .availableBalance(account.getAvailableBalance())
                                .ledgerBalance(account.getLedgerBalance())
                                .userId(account.getUser() != null ? account.getUser().getId() : null)
                                .userName(account.getUser() != null ? account.getUser().getUsername() : null)
                                .openDate(account.getOpenDate())
                                .closeDate(account.getCloseDate())
                                .createdBy(account.getCreatedBy())
                                .updatedBy(account.getUpdatedBy())
                                .build();
        }

        @GetMapping("/user/{userId}")
        public ResponseEntity<?> getAccountsByUserId(
                        @PathVariable Long userId) {

                String ref = generateReference();

                traceLogger.logTrace(
                                "Processing /api/accounts/user/" + userId + " request");

                List<Account> accounts = accountRepository.findByUser_Id(userId);
                return getResponseEntity(ref, accounts);
        }

        @GetMapping("/detail")
        public ResponseEntity<?> getAllAccountsDetail(
                        @RequestParam(required = false) String accountNo) {

                String ref = generateReference();

                traceLogger.logTrace("Processing /api/accounts/detail +"
                                + " request : ref=" + ref);

                if (accountNo != null && !accountNo.isBlank()) {
                        var accountOpt = accountRepository.findByAccountNo(accountNo);
                        if (accountOpt.isPresent()) {
                                AccountResponse resp = toResponse(accountOpt.get());
                                String msg = messageSource.getMessage(
                                                "response.success.message",
                                                null,
                                                "Account retrieved successfully",
                                                LocaleContextHolder.getLocale());
                                return ResponseEntity.ok(
                                                BaseResponse.success(ref, msg, resp));
                        } else {
                                String msg = messageSource.getMessage(
                                                "response.notfound.message",
                                                null,
                                                "Account not found",
                                                LocaleContextHolder.getLocale());
                                return ResponseEntity.ok(
                                                BaseResponse.success(ref, msg, null));
                        }
                }

                List<Account> accounts = accountRepository.findAll();
                return getResponseEntity(ref, accounts);
        }

        private ResponseEntity<?> getResponseEntity(String ref, List<Account> accounts) {
                List<AccountResponse> accountResponses = accounts.stream()
                                .map(this::toResponse)
                                .toList();

                if (accountResponses.isEmpty()) {

                        String msg = messageSource.getMessage(
                                        "response.notfound.message",
                                        null,
                                        "Account not found",
                                        LocaleContextHolder.getLocale());

                        return ResponseEntity.ok(
                                        BaseResponse.success(ref, msg, null));
                }

                String msg = messageSource.getMessage(
                                "response.success.message",
                                null,
                                "Accounts retrieved successfully",
                                LocaleContextHolder.getLocale());

                return ResponseEntity.ok(
                                BaseResponse.success(ref, msg, accountResponses));
        }

}
