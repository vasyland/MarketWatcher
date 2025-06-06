package com.stock.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stock.model.SymbolHistoryPrice;

@Repository
public interface HistoryPriceRepository extends CrudRepository<SymbolHistoryPrice, Long> {

}
