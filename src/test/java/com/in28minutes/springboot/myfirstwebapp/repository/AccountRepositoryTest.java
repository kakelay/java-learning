package com.in28minutes.springboot.myfirstwebapp.repository;

import com.in28minutes.springboot.myfirstwebapp.entity.Account;
import com.in28minutes.springboot.myfirstwebapp.entity.User;
import com.in28minutes.springboot.myfirstwebapp.entity.enums.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindAccountsByUserId() {
        User user = User.builder()
                .username("john")
                .password("password")
                .email("john@example.com")
                .build();

        user = entityManager.persistAndFlush(user);

        Account account = Account.builder()
                .user(user)
                .accountNo("ACC-1001")
                .accountName("Savings Account")
                .accountType("SAVINGS")
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();

        entityManager.persistAndFlush(account);

        List<Account> accounts = accountRepository.findByUser_Id(user.getId());
        entityManager.clear();

        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getAccountNo()).isEqualTo("ACC-1001");
        assertThat(accounts.get(0).getUser().getUsername()).isEqualTo("john");
    }
}
