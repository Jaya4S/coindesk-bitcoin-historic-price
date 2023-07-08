package kumarijaya.bitcoinhistoricprice.services;

import kumarijaya.bitcoinhistoricprice.response.BitcoinHistoricPriceResponse;
import kumarijaya.bitcoinhistoricprice.response.coindesk.CoindeskSupportedCurrenciesResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface BitcoinPriceService {
    ResponseEntity<BitcoinHistoricPriceResponse> getHistoricBitcoinPrices(String startDate, String endDate, String currency) throws IOException;

    List<CoindeskSupportedCurrenciesResponse> getSupportedCurrencies();

    boolean validateForSupportedCurrencies(String currency) throws IOException;
}
