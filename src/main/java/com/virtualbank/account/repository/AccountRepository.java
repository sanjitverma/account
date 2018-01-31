package com.virtualbank.account.repository;

import com.virtualbank.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by SANJIT on 13/01/18.
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
