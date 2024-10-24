package com.stock.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.model.CombinedSymbolData;
import com.stock.model.FmpSymbolData;
import com.stock.model.StockExchange;
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
		List<StockExchange> symbolList = symbolService.findAllSymbols();
		log.info("Symbols to process: " + symbolList);
		
		/* Getting watched symbols with defined yield range */
		List<WatchSymbol>  watchedSymbols = symbolService.getWatchSymbolsData();

		
		/* He we need to select all exchanges and query each exchange for data and then to combine them into one big list */
		Set<String> uniqueExchanges = symbolList.stream()
			    .map(StockExchange::exchange)  // Extract the exchange field
			    .collect(Collectors.toSet());  // Collect unique exchanges into a Set

		List<String> exchangeList = new ArrayList<>(uniqueExchanges);
		log.info("Exchanges: " + exchangeList);
		
		List<FmpSymbolData> allExchangesData = new ArrayList<>(); 
		
		/* Loop via all exchanges and get data into one buckle */
		for(int i=0; i< exchangeList.size(); i++) {
			if(exchangeList.get(i) == null) {
				log.error("Null found in exchange field in one of the symbols. Review watch_symbol table.");
				continue;
			}
			
			/*  Getting last price for the symbol from fmp provider  */
			List<FmpSymbolData> exchangeData = fmpDataProvider.getAllExchangeData(exchangeList.get(i));
			if (exchangeData != null) {
				allExchangesData.addAll(exchangeData);
			}
		}
		
        log.info("Data count: " + allExchangesData.size());
		
		List<CombinedSymbolData> combinedList = filterAndCombineData(watchedSymbols, allExchangesData);
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
