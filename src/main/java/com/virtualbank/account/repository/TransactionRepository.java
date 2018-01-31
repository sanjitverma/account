package com.virtualbank.account.repository;

import com.virtualbank.account.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by SANJIT on 13/01/18.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t where t.debitAccount = :id")
    Optional<List<Transaction>> findTransactionByDebitAccount(@Param("id") Long id);
}
