package kumarijaya.bitcoinhistoricprice.services;

import kumarijaya.bitcoinhistoricprice.response.BitcoinHistoricPriceResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface BitcoinPriceService {
    public ResponseEntity<BitcoinHistoricPriceResponse> getHistoricBitcoinPrices(String startDate, String endDate, String currency) throws IOException;
    public boolean validateForSupportedCurrencies(String currency) throws IOException;
}
