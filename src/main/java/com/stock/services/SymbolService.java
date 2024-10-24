package com.stock.services;

import java.util.List;

import com.stock.model.StockExchange;
import com.stock.model.SymbolStatus;
import com.stock.model.WatchSymbol;

public interface SymbolService {

//	List<StockExchange> getSymbols();
	
	List<WatchSymbol> getWatchSymbolsData();
	void cleanSymbolStatus();
	Iterable<SymbolStatus> saveSymbolStatuses(List<SymbolStatus> s);
	
	void deleteSymbolStatusWithTO();
	void deleteSymbolStatusWithoutTO();
	void truncateData();
	/**
	 * Get a list of symbols for processing
	 */
	List<StockExchange> findAllSymbols();
	
}
