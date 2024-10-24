package com.stock.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.stock.model.StockExchange;
import com.stock.model.SymbolStatus;
import com.stock.model.WatchSymbol;
import com.stock.repositories.SymbolStatusRepository;
import com.stock.repositories.WatchSymbolRepository;

import lombok.extern.slf4j.Slf4j;

@Service
//@RequiredArgsConstructor
@Slf4j
public class SymbolServiceImpl implements SymbolService {

	@Autowired
	private WatchSymbolRepository watchSymbolRepository;
	@Autowired
	private SymbolStatusRepository symbolStatusRepository;

	/**
	 * Get a list of symbols for processing
	 */
	@Override
	public List<StockExchange> findAllSymbols() {
	    List<Object[]> results = watchSymbolRepository.findAllSymbolsNative();
	    return results.stream()
	                  .map(row -> new StockExchange((String) row[0], (String) row[1]))
	                  .collect(Collectors.toList());
	}
	
	@Override
	public List<WatchSymbol> getWatchSymbolsData() {
		Iterable<WatchSymbol> p = watchSymbolRepository.findAll();
		List<WatchSymbol> ws = Streamable.of(p).toList();
		log.info("Number of selected watch symbols is: " + ws.size());
		return ws;
	}

	@Override
	public void cleanSymbolStatus() {
		symbolStatusRepository.deleteAll();
	}

	@Override
	public Iterable<SymbolStatus> saveSymbolStatuses(List<SymbolStatus> s) {
		return symbolStatusRepository.saveAll(s);
	}

	@Override
	public void deleteSymbolStatusWithTO() {
		symbolStatusRepository.deleteSymbolsEndingWithTO();
	}

	@Override
	public void deleteSymbolStatusWithoutTO() {
		symbolStatusRepository.deleteSymbolsEndingWithoutTO();
	}

	@Override
	public void truncateData() {
		symbolStatusRepository.truncateData();
	}
}
