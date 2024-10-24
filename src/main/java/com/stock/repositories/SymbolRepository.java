package com.stock.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stock.model.StockExchange;

@Repository
public class SymbolRepository {

	@Autowired
    private WatchSymbolRepository watchSymbolRepository;
	
	public List<StockExchange> findAllSymbols() {
	    List<Object[]> results = watchSymbolRepository.findAllSymbolsNative();
	    return results.stream()
	                  .map(row -> new StockExchange((String) row[0], (String) row[1]))
	                  .collect(Collectors.toList());
	}


}
