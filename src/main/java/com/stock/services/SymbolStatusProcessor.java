package com.stock.services;

import java.util.List;

import com.stock.model.CombinedSymbolData;

public interface SymbolStatusProcessor {

	void processSymbols(List<CombinedSymbolData> data); 
	
}
