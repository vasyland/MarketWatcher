package com.stock.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "listed_companies", indexes = {
		@Index(name = "idx_listed_symbol", columnList = "symbol")
})
@Getter
@Setter
@NoArgsConstructor
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
}
