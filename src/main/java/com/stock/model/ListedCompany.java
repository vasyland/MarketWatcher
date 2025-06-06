package com.stock.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "listed_companies", indexes = {
		@Index(name = "idx_listed_symbol", columnList = "symbol")
})
public class ListedCompany implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="symbol", nullable = false, length = 11)
	private String symbol;

	@Column(name="name", length = 100)
	private String name;
	
	@Column(name="marketcap")
	private Long marketCap;
	
	@Column(name="exchange", length = 6)
    private String exchange;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(Long marketCap) {
		this.marketCap = marketCap;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ListedCompany [symbol=" + symbol + ", name=" + name + ", marketCap=" + marketCap + ", exchange="
				+ exchange + "]";
	}
	
	
}
