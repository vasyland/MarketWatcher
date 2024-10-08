package com.stock.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.stock.model.SymbolStatus;
import com.stock.model.WatchSymbol;
import com.stock.repositories.SymbolRepository;
import com.stock.repositories.SymbolStatusRepository;
import com.stock.repositories.WatchSymbolRepository;

import lombok.extern.slf4j.Slf4j;

@Service
//@RequiredArgsConstructor
@Slf4j
public class SymbolServiceImpl implements SymbolService {

//	@Autowired
//	private SymbolRepository symbolRepository;
	
	@Autowired
	private WatchSymbolRepository watchSymbolRepository;
	@Autowired
	private SymbolStatusRepository symbolStatusRepository;
	
	
//	public SymbolServiceImpl(SymbolRepository symbolRepository, WatchSymbolRepository watchSymbolRepository,
//			SymbolStatusRepository symbolStatusRepository) {
//		super();
//		this.symbolRepository = symbolRepository;
//		this.watchSymbolRepository = watchSymbolRepository;
//		this.symbolStatusRepository = symbolStatusRepository;
//	}
	
	
	@Override
	public List<String> getSymbols() {
		return watchSymbolRepository.findAllSymbols();
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
}
