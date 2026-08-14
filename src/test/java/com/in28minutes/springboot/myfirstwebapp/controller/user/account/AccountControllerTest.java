package com.in28minutes.springboot.myfirstwebapp.controller.user.account;

import com.in28minutes.springboot.myfirstwebapp.entity.Account;
import com.in28minutes.springboot.myfirstwebapp.entity.User;
import com.in28minutes.springboot.myfirstwebapp.entity.enums.AccountStatus;
import com.in28minutes.springboot.myfirstwebapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountControllerTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldLoadUserNameWithoutLazyInitializationIssue() {
        User user = User.builder()
                .username("johndoe")
                .password("password")
                .email("johndoe@example.com")
                .build();

        user = entityManager.persistAndFlush(user);

        Account account = Account.builder()
                .user(user)
                .accountNo("ACC-2001")
                .accountName("Savings Account")
                .accountType("SAVINGS")
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();

        entityManager.persistAndFlush(account);

        List<Account> accounts = accountRepository.findByUser_Id(user.getId());
        entityManager.clear();

        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getUser().getUsername()).isEqualTo("johndoe");
    }
}
