package com.banking.bankingDemo.repository;

import com.banking.bankingDemo.dto.CustomerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "customerHistories", exported = false, path = "customerHistories")
public interface CustomerHistoryRepository extends JpaRepository<CustomerHistory, Long> {

    CustomerHistory findByCustomerId(long customerId);
}

