package com.pocket.currencies.pocket.repository;

import com.pocket.currencies.pocket.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
