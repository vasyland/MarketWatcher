package com.stock.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stock.model.ListedCompany;

@Repository
public interface ListedCompanyRepository extends JpaRepository<ListedCompany, String> {
	List<ListedCompany> findBySymbol(String symbol);
}
