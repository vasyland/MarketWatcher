package com.stock.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stock.model.WatchSymbol;

@Repository
public interface WatchSymbolRepository extends CrudRepository<WatchSymbol, String> {

	@Query(value = "SELECT symbol FROM watch_symbol", nativeQuery = true)
    List<String> findAllSymbols();
}
