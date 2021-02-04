package com.boa.trading.currencyapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boa.trading.currencyapi.models.Currency;

public interface CurrencyRepository extends JpaRepository<Currency,String>{

}
