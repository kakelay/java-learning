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
                                .accountType(account.getAccountType())
                                .accountCode(account.getProductCode())
                                .build();
        }

        @GetMapping("/user/{userId}")
        public ResponseEntity<?> getAccountsByUserId(
                        @PathVariable Long userId) {

                String ref = generateReference();

                traceLogger.logTrace(
                                "Processing /api/accounts/cid/" + userId + " request");

                List<Account> accounts = accountRepository.findByUser_Id(userId);
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
