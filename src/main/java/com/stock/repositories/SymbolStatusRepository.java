package com.stock.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stock.model.SymbolStatus;

import jakarta.transaction.Transactional;

@Repository
public interface SymbolStatusRepository extends CrudRepository<SymbolStatus, String> {
	
	// Native query to delete all records where symbol ends with '.TO'
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM symbol_status WHERE symbol LIKE '%.TO'", nativeQuery = true)
    void deleteSymbolsEndingWithTO();
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM symbol_status WHERE symbol NOT LIKE '%.TO'", nativeQuery = true)
    void deleteSymbolsEndingWithoutTO();
}
