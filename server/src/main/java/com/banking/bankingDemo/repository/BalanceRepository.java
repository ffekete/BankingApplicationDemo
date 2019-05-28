package com.banking.bankingDemo.repository;

import com.banking.bankingDemo.dto.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "balances", exported = false, path = "balances")
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Balance findByCustomerId(long customerId);
}

