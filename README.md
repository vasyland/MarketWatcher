# MarketWatcher
Market Watcher TSX only
```
1. This application reads stock settings from watch_symbol table and calculates current yield for each symbol.
Result of calculation is stored in the symbol_status table.

2. Current Yield is calculated based on current price taken via API from https://fmpcloud.io.
There are three end points for each exchange because symbols are listed on different exchanges.

3. All prices from all exchanges are loaded into one java collection and the n current price is extracted for each symbol during processing of one symbol.
Free plan allows only 250 calls per day on limited time. But for this application 20 calls is enough. Keep in mind that 3 calls are happenning due to symbols are listed on diffreent exchanges.

4. This application runs under Quartz control several times a day from Monday to Friday. 3-4 times is enough.

5. This application can be run on local laptop and there is no need to deploy it to the cloud. 

```

## Used Data Source
https://financialmodelingprep.com/api/v3/symbol/TSX?apikey=ATt4kh10v7qTrdhbmSvWWOJmpYLgMIy5
https://financialmodelingprep.com/api/v3/symbol/NYSE?apikey=ATt4kh10v7qTrdhbmSvWWOJmpYLgMIy5
https://financialmodelingprep.com/api/v3/symbol/NASDAQ?apikey=ATt4kh10v7qTrdhbmSvWWOJmpYLgMIy5
