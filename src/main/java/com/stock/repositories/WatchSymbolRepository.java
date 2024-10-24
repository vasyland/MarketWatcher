package com.stock.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stock.model.StockExchange;
import com.stock.model.WatchSymbol;

@Repository
public interface WatchSymbolRepository extends CrudRepository<WatchSymbol, String> {

//	@Query(value = "SELECT symbol, exchange FROM watch_symbol", nativeQuery = true)
//    List<StockExchange> findAllSymbols();
	
//	@Query(value = "SELECT new com.stock.model.StockExchange(symbol, exchange) FROM watch_symbol")
//	List<StockExchange> findAllSymbols();

	@Query(value = "SELECT symbol, exchange FROM watch_symbol", nativeQuery = true)
	List<Object[]> findAllSymbolsNative();

	
}
