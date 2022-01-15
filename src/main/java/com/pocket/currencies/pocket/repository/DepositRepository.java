package com.pocket.currencies.pocket.repository;

import com.pocket.currencies.pocket.entity.Deposit;
import com.pocket.currencies.pocket.entity.Pocket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends PagingAndSortingRepository<Deposit, Long> {

    List<Deposit> getAllByPocket(Pocket pocket, Pageable pageable);
    Integer countAllByPocket(Pocket pocket);
}
