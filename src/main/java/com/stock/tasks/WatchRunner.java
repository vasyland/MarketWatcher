package com.stock.tasks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.model.CombinedSymbolData;
import com.stock.model.CurrentPrice;
import com.stock.model.FmpSymbolData;
import com.stock.model.ListedCompany;
import com.stock.model.StockExchange;
import com.stock.model.WatchSymbol;
import com.stock.repositories.ListedCompanyRepository;
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
	
	@Autowired
	ListedCompanyRepository listedCompanyRepository;
	
	@Value("${cron-string}")
	private String cron;
	
	@Scheduled(cron = "${cron-string}")
	public void runJob() {
		
		log.info("Scheduled: " + cron);
		
		/* Getting List of symbol from db to process */
		List<StockExchange> symbolList = symbolService.findAllSymbols();
		log.info("Symbols to process: " + symbolList);
		
		/* Getting watched symbols with defined yield range */
		List<WatchSymbol>  watchedSymbols = symbolService.getWatchSymbolsData();
		
		/* Here we need to select all exchanges and query each exchange for data and then to combine them into one big list */
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
			
			log.info("[WatchRunner:runJob] Exchange: " + exchangeList.get(i) + " Data count: " + exchangeData.size());
			
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
		
		/* Build a list of symbol + price items to save them to db as history price */
		symbolService.saveStockPrices(combinedList);
		
		symbolStatusProcessor.processSymbols(combinedList);
		log.info("Process Status Cycle Complete.");
		
		/* 
		 * Populate ListedCompanies 
		 * Needs to clean up more from companies that will nevver be invested into
		 * because:
		 * 1. Low volume
		 * 2. No earnings per share
		 * 3. Low market cap
		 * 4. Price less than .5		 * 5. Name is too long
		 * 6. Name contains ETF
		 * 7. Symbol is too long
		 * 8. Market cap is null or empty
		 * 9. Market cap is 0
		 * 10. Volume is less than 1000
		 * */
		
		// Source: https://fmpcloud.io/api/v3/symbol/TSX?apikey=ATt4kh10v7qTrdhbmSvWWOJmpYLgMIy5
//		List<ListedCompany> listedCompanyData = new ArrayList<>();
//		int countSkipped = 0; 
//		
//		for( FmpSymbolData data : allExchangesData) {
//			
//			if (data.name().length() > 100 
//					|| data.name().contains("ETF")
//					|| data.name().toUpperCase().contains("ETF")
//					|| data.symbol().length() > 10 
//					|| data.marketCap() == null 
//					|| data.marketCap().isEmpty() 
//					|| data.marketCap().equals("0")
//					) {
////				log.error("Name is too long: " + data.name() + "Market Cap = " + data.marketCap());	
//				countSkipped++;
//				continue;
//			}
//			
//			int volume = Integer.parseInt(data.volume());
//			if (volume < 1000) {
//				countSkipped++;
//				continue;
//			}
//			
//			// check if price less than 1
//			if (data.price() < .5) {
//				countSkipped++;
//				continue;
//			}
//			
//			ListedCompany lc = new ListedCompany();
//			lc.setSymbol(data.symbol());
//			lc.setName(data.name());
//			lc.setMarketCap(Long.parseLong(data.marketCap()));	
//			lc.setExchange(data.exchange());				
//			listedCompanyData.add(lc);
//		}
//		listedCompanyRepository.saveAll(listedCompanyData);
//		log.info("ListedCompany data saved and updated. Skipped: " + countSkipped);
		
		/* Get a list of listed companies from db*/
		List<ListedCompany> listedCompanyData = new ArrayList<>();
		listedCompanyData = listedCompanyRepository.findAll();
		log.info("ListedCompany data count: " + listedCompanyData.size());
		
		
		
		// Filter current prices for listed companies only
		List<FmpSymbolData> filterredFmpData = filterDataForCurrentPrice(listedCompanyData, allExchangesData);
		List<CurrentPrice> currentPriceList = new ArrayList<>();

		// Populate current price list from fmp data
		for( FmpSymbolData data : filterredFmpData) {
			CurrentPrice cp = new CurrentPrice();
			
			cp.setSymbol(data.symbol());
			cp.setPrice(new BigDecimal(data.price()));
			cp.setPrice_change(new BigDecimal(data.change()));
			cp.setCreatedOn(LocalDateTime.now());

			currentPriceList.add(cp);
		}
		log.info("Current price data count: " + currentPriceList.size());
		/* Save current price data to db */
		if (currentPriceList.size() > 0) {
			symbolService.resetPrices();
			symbolService.saveSymbolCurrentPrices(currentPriceList);
		} else {
			log.error("No current price data to save.");
		}
		log.info("Current price data load completed.");
		return;
	}
		
	
	/* Get Data for symbols of all listed only  companies */
	public static List<FmpSymbolData> filterDataForCurrentPrice(List<ListedCompany> smallList, List<FmpSymbolData> bigList) {
        // Get only bigList elements where the symbol exists in smallList
        return bigList.stream()
                .filter(bigData -> smallList.stream()
                        .anyMatch(smallData -> smallData.getSymbol().equals(bigData.symbol())))
                .collect(Collectors.toList());
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
