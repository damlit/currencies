package com.pocket.currencies.pocket.repository;

import com.pocket.currencies.pocket.entity.Pocket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PocketRepository extends JpaRepository<Pocket, Long> {
}
