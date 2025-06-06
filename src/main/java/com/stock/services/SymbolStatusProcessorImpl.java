package com.stock.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stock.model.CombinedSymbolData;
import com.stock.model.SymbolStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SymbolStatusProcessorImpl implements SymbolStatusProcessor {

	@Value("${fmp.exchange}")
    private String exchange;
	
	@Autowired
	private SymbolService symbolService;
	
	@Override
	public void processSymbols(List<CombinedSymbolData> data) {

		if(data == null) {
			log.error("No data found for processing status.");
			return;
		}
		
		int res;
		int res2;
		int res3;
		
		String action = "";
		List<SymbolStatus> result = new ArrayList<>();
		
		for(CombinedSymbolData csd : data) {
			
			/* convert price from Double into BigDecimal */
			BigDecimal currentPrice = BigDecimal.valueOf(csd.price());
			
			/* Current Yield */
			BigDecimal yield = csd.quoterlyDividendAmount().multiply(BigDecimal.valueOf(400))
		            .divide(currentPrice, RoundingMode.HALF_EVEN);
			BigDecimal yieldRange = csd.upperYield().subtract(csd.lowerYield());
			BigDecimal quoterOfUpperYield =  yieldRange.divide(BigDecimal.valueOf(6), 3, RoundingMode.HALF_EVEN);
			BigDecimal allowedBuyYield = csd.upperYield().subtract(quoterOfUpperYield);
			BigDecimal sellPointYield = csd.lowerYield().add(quoterOfUpperYield);
			
			BigDecimal bestBuyPrice = csd.quoterlyDividendAmount().multiply(BigDecimal.valueOf(400)).divide(csd.upperYield(), RoundingMode.HALF_EVEN);
			BigDecimal allowedBuyPrice = csd.quoterlyDividendAmount().multiply(BigDecimal.valueOf(400)).divide(allowedBuyYield, RoundingMode.HALF_EVEN);
			
			BigDecimal overpricedAmount = currentPrice.subtract(bestBuyPrice).setScale(4, RoundingMode.HALF_UP);
			
			BigDecimal overpricedPercentage = BigDecimal.ZERO;
			if (currentPrice.compareTo(BigDecimal.ZERO) > 0) {
			    overpricedPercentage = (currentPrice.subtract(bestBuyPrice))
			            .divide(currentPrice, RoundingMode.HALF_EVEN)
			            .multiply(BigDecimal.valueOf(100));
			}
			
//			BigDecimal overpricedPercentage2 = (currentPrice.subtract(bestBuyPrice))
//					.divide(currentPrice).multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP);
			
			BigDecimal sellPrice = csd.quoterlyDividendAmount().multiply(BigDecimal.valueOf(400)).divide(csd.lowerYield(), RoundingMode.HALF_EVEN);
			log.info("\n" + csd.symbol() + "  Price:" + currentPrice +
					"  QDivAmt: " + csd.quoterlyDividendAmount() +
					"  Yield: " + yield +
					"  \n         Upper Yield: " + csd.upperYield() +
					"  Lower Yield: " + csd.lowerYield() +
					"  Quoter of Yield Range: " + quoterOfUpperYield +
					"  Allowed to Buy Yield: " + allowedBuyYield +
					"  Allowed to Buy Price: " + allowedBuyPrice +
					"  Best Buy Price: " + bestBuyPrice +
					"  Overpriced Amount: " + overpricedAmount +
					"  Overpriced Percentage: " + overpricedPercentage +
					
					"  Sell Point Yield: " + sellPointYield +
					"  Sell Price: " + sellPrice);
			/* 
		     * Action = "Buy" if current yield is above Upper yield or in the top of 1/4th of the range
		     * between Upper and Lower yields 
		     */
		    res = yield.compareTo(allowedBuyYield);
		    res2 = csd.upperYield().compareTo(BigDecimal.valueOf(0.0));
		    if (res == 0 || res == 1 && res2 != 0) {
		       action = "Buy";
		    } else {
		       action = "";
		    }
		    
		    res3 = yield.compareTo(sellPointYield);
		    if (res3 == -1 && res2 != 0) {
			   action = "Sell";
		    }
		    
		    if(res == -1 && res3 == 1 && res2 != 0) {
		    	action = "Hold";
		    }
		    
		    if(res2 == 0) {
		    	action = "N/A";
		    }
			
		    log.info("\n" + csd.symbol() + "  Price:" + currentPrice + 
		    		"  QDivAmt: " + csd.quoterlyDividendAmount() + 
		    		"  Yield: " + yield + 
		    		"  \n         Upper Yield: " + csd.upperYield() +
		    		"  Lower Yield: " + csd.lowerYield() +
		    		"  Quoter of Yield Range: " + quoterOfUpperYield +
		    		"  Allowed to Buy Yield: " + allowedBuyYield +
		    		"  Allowed to Buy Price: " + allowedBuyPrice +
		    		"  Best Buy Price: " + allowedBuyPrice +
		    		"  Action: " + action);		    
		    
		    /* Symbol Status data */
	    	SymbolStatus symbolStatus = new SymbolStatus();
	    	symbolStatus.setSymbol(csd.symbol());
		    symbolStatus.setCurrentPrice(currentPrice);
		    symbolStatus.setCurrentYield(yield);
		    symbolStatus.setAllowedBuyPrice(allowedBuyPrice);
		    symbolStatus.setBestBuyPrice(bestBuyPrice);
		    symbolStatus.setRecommendedAction(action);

		    if(res2 != 0) {
		    	symbolStatus.setQuoterlyDividendAmount(csd.quoterlyDividendAmount());
		    	symbolStatus.setUpperYield(csd.upperYield());
		    	symbolStatus.setLowerYield(csd.lowerYield());
		    	symbolStatus.setAllowedBuyYield(allowedBuyYield);
		    	symbolStatus.setSellPointYield(sellPointYield);
		    }
		    
		    LocalDateTime ldt = LocalDateTime.now();
		    symbolStatus.setUpdatedOn(ldt);
		    result.add(symbolStatus);
		}
		
		/* Clean table from the records  */
		symbolService.truncateData();
		log.info("Cleaning table done. Exchange = " + exchange);
		log.info("Number of processed symbols: " + result.size());
		
		/* Saving calculations into table  */
		Iterable<SymbolStatus> j = symbolService.saveSymbolStatuses(result);
		log.info("SYMBOL_STATUS table populated with new data.");
	    return;
	}

}
