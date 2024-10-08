package com.stock.model;

import java.math.BigDecimal;

public record CombinedSymbolData(
		String symbol,
		Double price,
		BigDecimal quoterlyDividendAmount,
		BigDecimal upperYield,
		BigDecimal lowerYield
		) {}
