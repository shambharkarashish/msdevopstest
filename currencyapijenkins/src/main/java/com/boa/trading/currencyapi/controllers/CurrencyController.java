package com.boa.trading.currencyapi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boa.trading.currencyapi.models.Currency;
import com.boa.trading.currencyapi.services.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {
	@Autowired
	private CurrencyService currencyService;
	
	//add currency
	@PostMapping({"/v1.0", "/v1.1"})
	public ResponseEntity<?> addCurrency(@RequestBody Currency currency)
	{
		Currency currencyObj=this.currencyService.addCurrency(currency);
		if(currencyObj!=null)
		{
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(currencyObj);
			
		}
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Currency not created validate the input");
		
		
	}
	@GetMapping({"/v1.0", "/v1.1"})
	public List<Currency> getAllCurrencies()
	{
		return this.currencyService.selectAllCurrencies();
	}
	
	@GetMapping({"/v1.0/{code}", "/v1.1/{code}"})
	public ResponseEntity<?> getCurrencyById(@PathVariable("code") String code)
	{
		Currency currency=this.currencyService.selectCurrencyById(code);
		if(currency!=null)
		{
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(currency);
			
		}
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Currency Record not found");
					
	}
	
	@DeleteMapping({"/v1.0/{code}", "/v1.1/{code}"})
	public ResponseEntity<?> deleteCurrency(@PathVariable("code") String code)
	{
		boolean status=this.currencyService.deleteCurrency(code);
		if(status)
		{
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(code+"deleted...");
			
		}
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code Not Existing, not deleted");
	}
	
	
	//update currency
		@PutMapping({"/v1.0", "/v1.1"})
		public ResponseEntity<?> updateCurrency(@RequestBody Currency currency)
		{
			Currency currencyObj=this.currencyService.updateCurrency(currency);
			if(currencyObj!=null)
			{
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(currencyObj);
				
			}
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Currency not created validate the input");
			
			
		}
	
	
		//squiggly filter
		 //squiggly filter
	    //http://localhost:7076/currencies/filters/v1.0/INR?fields=description,symbolLocation
	    //http://localhost:7076/currencies/filters/v1.0/INR?fields=-description,-symbolLocation
		//http://localhost:7076/currencies/filters/v1.0/INR?fields=code,tradeableFlag
	    @CrossOrigin("*")
		@GetMapping({"/filters/v1.0/{code}", "/filters/v1.1/{code}"})
		public ResponseEntity<?> findCurrencyById(@RequestParam(name = "fields", required = false) 
	    String fields, @PathVariable("code") String code)
		{
	    	
	    	Map<Object,Object> model=new HashMap<>();
	    	
	    	Currency currency= this.currencyService.selectCurrencyById(code);
	    	
	    	if(currency!=null)
	    	{
	    		//fields refers to runtime selection
	    		ObjectMapper mapper = Squiggly.init(new ObjectMapper(), fields);  		
				return ResponseEntity.ok(SquigglyUtils.stringify(mapper, currency));

	    	}
	    	else
	    	{
		         model.put("message", "currency not existing");
			        
		         return ResponseEntity.ok(model);
	    	}

		}
	
	    //conditional query
	    
		//http://localhost:7076/currencies/condition/v1.0?q=code==U*
	    //http://localhost:7076/currencies/condition/v1.0?q=code=lt=I*
	    //http://localhost:7076/currencies/condition/v1.0?q=code=lt=I*;code=gt=E*
		 @GetMapping({"/condition/v1.0", "/condition/v1.1"})
		    public ResponseEntity<List<Currency>> query(@RequestParam(value = "q") String query) {
		        List<Currency> result = null;
		        try {
		         result= currencyService.searchByQuery(query);
		        }catch (IllegalArgumentException iae){
		            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                    .body(result);
		        }
		        return  ResponseEntity.status(HttpStatus.OK)
		                .body(result);
		    }
		
		 
		 //paging and sorting
		 //http://localhost:7076/currencies/paging/v1.0?pageNo=1&sortKey=code
			@GetMapping({"/paging/v1.0", "/paging/v1.1"})
			public List<Currency> getAllCurrenciesByPageandSort(@RequestParam(value="pageNo", defaultValue="0") Integer pageNo,
				      @RequestParam(value="sortKey", defaultValue="code") String sortKey)
			{
				return this.currencyService.getAllCurrencies(pageNo, sortKey);
			}
	

}
