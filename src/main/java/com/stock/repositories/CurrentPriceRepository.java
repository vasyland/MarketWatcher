package com.stock.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stock.model.CurrentPrice;

@Repository
public interface CurrentPriceRepository extends JpaRepository<CurrentPrice, Integer> {
	List<CurrentPrice> findBySymbol(String symbol);
	CurrentPrice findFirstBySymbol(String symbol);
	Set<CurrentPrice> findBySymbolIn(List<String> symbols);
	
	@Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE current_price", nativeQuery = true)
    void truncateTable();
}
