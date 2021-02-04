package com.boa.trading.currencyapi.services;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.boa.trading.currencyapi.models.Currency;
import com.boa.trading.currencyapi.repositories.CurrencyRepository;
import com.github.tennaito.rsql.jpa.JpaCriteriaCountQueryVisitor;
import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurrencyService {
    @Autowired
	private CurrencyRepository  currencyRepository;
    @Autowired
    private EntityManager entityManager;
    //create
    public Currency addCurrency(Currency currency)
    {
    	return this.currencyRepository.save(currency);
    }
    
    
    //update
    
    public Currency updateCurrency(Currency currency)
    {
    	return this.currencyRepository.save(currency);
    }
    
    //delete
    public boolean deleteCurrency(String currencyCode)
    {
    	boolean status=false;
    	this.currencyRepository.deleteById(currencyCode);
    	if(this.selectCurrencyById(currencyCode)!=null)
    		status=true;
    	return status;
    	
    }
    
    //select all
    
    public List<Currency> selectAllCurrencies()
    {
    	return this.currencyRepository.findAll();
    	
    	
    }
    
    //select curr by code
    
    public Currency selectCurrencyById(String currencyCode)
    {
    	return this.currencyRepository.findById(currencyCode).orElse(null);
    	
    	
    }
    
    //conditional query
    //
    public List<Currency> searchByQuery(String queryString) {
        RSQLVisitor<CriteriaQuery<Currency>, EntityManager> visitor = new JpaCriteriaQueryVisitor<Currency>();
        CriteriaQuery<Currency> query;
        query = getCriteriaQuery(queryString, visitor);
        List<Currency> resultList = entityManager.createQuery(query).getResultList();
        if (resultList == null || resultList.isEmpty()){
            return Collections.emptyList();
        }
        return resultList;
    }

   
    public Long countByQuery(String queryString) {
        RSQLVisitor<CriteriaQuery<Long>, EntityManager> visitor = new JpaCriteriaCountQueryVisitor<Currency>();
        CriteriaQuery<Long> query;
        query = getCriteriaQuery(queryString, visitor);

        return entityManager.createQuery(query).getSingleResult();
    }

    private <T> CriteriaQuery<T> getCriteriaQuery(String queryString, RSQLVisitor<CriteriaQuery<T>, EntityManager> visitor) {
        Node rootNode;
        CriteriaQuery<T> query;
        try {
            rootNode = new RSQLParser().parse(queryString);
            query = rootNode.accept(visitor, entityManager);
        }catch (Exception e){
            log.error("An error happened while executing RSQL query", e);
            throw new IllegalArgumentException(e.getMessage());
        }
        return query;
    }
    
    //paging and sorting
  //paging and Sorting
    public List<Currency> getAllCurrencies(Integer pageNo, String sortKey)
	{
		// Setting no. of records in each page, page no., sort field
		int noOfRecords = 2;
		Pageable page = PageRequest.of(pageNo, noOfRecords, Sort.by(sortKey));
		Page<Currency> pagedResult = currencyRepository.findAll(page);
		// changing to List
		return pagedResult.getContent();
	}
    
    
    
    
    
}
