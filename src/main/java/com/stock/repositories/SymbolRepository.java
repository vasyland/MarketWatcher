package com.stock.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SymbolRepository {

	@Autowired
    private WatchSymbolRepository watchSymbolRepository;
	
	public List<String> getAllSymbols() {
        return watchSymbolRepository.findAllSymbols();
    }

}
