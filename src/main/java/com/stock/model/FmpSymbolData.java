package com.stock.model;

public record FmpSymbolData( 
	 String symbol,
	 String name,
	 Double price,
	 String changesPercentage,
	 String change,
	 String dayLow,
	 String dayHigh,
	 String yearHigh,
	 String yearLow,
	 String marketCap,
	 String priceAvg50,
	 String priceAvg200,
	 String exchange,
	 String volume,
	 String avgVolume,
	 String open,
	 String previousClose,
	 String eps,
	 String pe,
	 String earningsAnnouncement,
	 String sharesOutstanding,
	 String timestamp
	) {}
