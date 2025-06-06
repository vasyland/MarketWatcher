package com.stock.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.stock.model.FmpSymbolData;
import reactor.core.publisher.Mono;

@Component
public class FmpDataProvider {
	
	private static final Logger log = LoggerFactory.getLogger(FmpDataProvider.class);

	@Value("${fmp.apiKey}")
    private String apiKey;
	
	@Value("${fmp.exchange}")
    private String exchange;
	
	private final WebClient webClient;
	
	// Constructor injection for WebClient
    public FmpDataProvider(WebClient webClient) {
        this.webClient = webClient;
    }
    
    public List<FmpSymbolData> getAllStockData() {
   	 try {
   		 String url = "/api/v3/symbol/"+ exchange + "?apikey=" + apiKey;
   		 log.info("[FmpDataProvider:getAllExchangeData] FmpDataProvider url = " + url);
   		 
            return webClient.get()
                    .uri("/api/v3/symbol/"+ exchange + "?apikey=" + apiKey)
                    .retrieve()
                    .bodyToFlux(FmpSymbolData.class)
                    .collectList()
                    .block(); // block() makes the call synchronous
        } catch (WebClientResponseException ex) {
            System.err.println("Error fetching stock data: " + ex.getResponseBodyAsString());
            return List.of(); // Return empty list if there is an error
        }
   }
    
    
    public List<FmpSymbolData> getAllExchangeData(String stockExchange) {
      	 try {
      		 String url = "/api/v3/symbol/"+ stockExchange + "?apikey=" + apiKey;
      		 log.info("[FmpDataProvider:getAllExchangeData] FmpDataProvider url = " + url);
      		 
               return webClient.get()
                       .uri("/api/v3/symbol/"+ stockExchange + "?apikey=" + apiKey)
                       .retrieve()
                       .bodyToFlux(FmpSymbolData.class)
                       .collectList()
                       .block(); // block() makes the call synchronous
           } catch (WebClientResponseException ex) {
               System.err.println("Error fetching exchange stock data: " + ex.getResponseBodyAsString());
               return List.of(); // Return empty list if there is an error
           }
      }
    
    
    /*
     * Return Mono<String> instead of void
     */
    public Mono<String> getStockQuote(String symbol) {
        String url = "/api/v3/quote-short/" + symbol + "?apikey=" + apiKey;
        log.info(" FmpDataProvider url = " + url);
        
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }
}
