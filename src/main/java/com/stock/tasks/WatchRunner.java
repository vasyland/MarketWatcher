package com.stock.tasks;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.model.CombinedSymbolData;
import com.stock.model.FmpSymbolData;
import com.stock.model.WatchSymbol;
import com.stock.services.FmpDataProvider;
import com.stock.services.SymbolService;
import com.stock.services.SymbolStatusProcessorImpl;

import lombok.extern.slf4j.Slf4j;

@Component
//@RequiredArgsConstructor
@Slf4j
public class WatchRunner {

	@Autowired
	private SymbolService symbolService;
	@Autowired
	FmpDataProvider fmpDataProvider;
	@Autowired
	SymbolStatusProcessorImpl symbolStatusProcessor;
	
	
	@Scheduled(cron = "${cron-string}")
	public void runJob() {
		
		/* Getting List of symbol from db to process */
		List<String> symbolList = symbolService.getSymbols();
		log.info("Symbols to process: " + symbolList);
		
		/* Getting watched symbols with defined yield range */
		List<WatchSymbol>  watchedSymbols = symbolService.getWatchSymbolsData();
				
		/*  Getting last price for the symbol from fmp provider  */
		List<FmpSymbolData> tsxData = fmpDataProvider.getAllStockData();
        log.info("Data count: " + tsxData.size());
		
//		tsxData.forEach(t -> {
//			if(t.price() != null && t.price() > Double.parseDouble("20") && t.price() < Double.parseDouble("100"))
//				System.out.println(t.symbol() + "   " + t.price());
//		});
		
//		List<FmpSymbolData> tsxtoProcessData = filterData(watchedSymbols, tsxData);
//		tsxtoProcessData.forEach(t -> {
//			if(t.price() != null && t.price() > Double.parseDouble("20") && t.price() < Double.parseDouble("100"))
//				System.out.println(t.symbol() + "   " + t.price());
//		});
		
		
		List<CombinedSymbolData> combinedList = filterAndCombineData(watchedSymbols, tsxData);
		combinedList.forEach(t -> {
				System.out.println(t.symbol() + " Price: " + t.price() + " Div.$: " + t.quoterlyDividendAmount()
				+ " UYield: " + t.upperYield() + "  LYield: " + t.lowerYield());
		});
		
		symbolStatusProcessor.processSymbols(combinedList);
		log.info("Process Status Cycle Complete.");
		
		return;
	}
	
	
	/* Get Data for symbols listed in WatchList */
	public static List<FmpSymbolData> filterData(List<WatchSymbol> smallList, List<FmpSymbolData> bigList) {
        // Get only bigList elements where the symbol exists in smallList
        return bigList.stream()
                .filter(bigData -> smallList.stream()
                        .anyMatch(smallData -> smallData.getSymbol().equals(bigData.symbol())))
                .collect(Collectors.toList());
    }
	
	
	/* Get combined list that contains yields and prices
	 * Method to filter and combine data from both lists 
	 * */
    public static List<CombinedSymbolData> filterAndCombineData(List<WatchSymbol> smallList, List<FmpSymbolData> bigList) {
        return bigList.stream()
                .flatMap(bigData -> smallList.stream()
                        .filter(smallData -> smallData.getSymbol().equals(bigData.symbol()))
                        .map(smallData -> new CombinedSymbolData(
                                bigData.symbol(),
                                bigData.price(),
                                smallData.getQuoterlyDividendAmount(),
                                smallData.getUpperYield(),
                                smallData.getLowerYield()
                                 )))
                .collect(Collectors.toList());
    }
}
